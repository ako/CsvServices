"""Attach Studio Pro style mx_metadata git notes to commits of an MPR v1 project.

Studio Pro records the Mendix version and the changed model units of every commit
in a git note under refs/notes/mx_metadata. Commits made with plain git have none.
`mxcli git note` computes these notes but only supports MPR v2 projects; this is a
port of its logic (cmd/mxcli/gitnote.go) that reads units from the Unit.Contents
column of an MPR v1 .mpr file.

usage (from the repository root):
  python3 tools/mx_git_note_v1.py [--write] <branch> <rev>...
  python3 tools/mx_git_note_v1.py --write claude/optimistic-feynman-9c6gpx $(git rev-list master..HEAD)
  git push origin refs/notes/mx_metadata

Without --write it prints "<rev><TAB><note json>" per commit.
"""
import json, sqlite3, struct, subprocess, sys, tempfile, uuid, os

def bson_doc(b, i=0):
    n = struct.unpack_from('<i', b, i)[0]; end = i + n - 1; i += 4; out = []
    while i < end:
        t = b[i]; i += 1
        k_end = b.index(b'\0', i); k = b[i:k_end].decode(); i = k_end + 1
        if t == 0x01: v = struct.unpack_from('<d', b, i)[0]; i += 8
        elif t == 0x02:
            l = struct.unpack_from('<i', b, i)[0]; v = b[i+4:i+4+l-1].decode('utf-8', 'replace'); i += 4 + l
        elif t in (0x03, 0x04):
            l = struct.unpack_from('<i', b, i)[0]; v = bson_doc(b, i); i += l
            if t == 0x04: v = [x[1] for x in v]
        elif t == 0x05:
            l = struct.unpack_from('<i', b, i)[0]; v = ('bin', b[i+5:i+5+l]); i += 5 + l
        elif t == 0x08: v = b[i] == 1; i += 1
        elif t in (0x09, 0x12, 0x11): v = struct.unpack_from('<q', b, i)[0]; i += 8
        elif t == 0x0A: v = None
        elif t == 0x10: v = struct.unpack_from('<i', b, i)[0]; i += 4
        else: raise ValueError(f'bson type {t:#x}')
        out.append((k, v))
    return out

def keys(v, acc):
    if isinstance(v, list):
        for x in v:
            if isinstance(x, tuple) and len(x) == 2 and isinstance(x[0], str) and x[0] != 'bin': acc.add(x[0]); keys(x[1], acc)
            else: keys(x, acc)
    return acc

def canon(v, drop):
    """Comparable form: element $IDs ignored, keys in `drop` removed."""
    if isinstance(v, list):
        if v and all(isinstance(x, tuple) and len(x) == 2 and isinstance(x[0], str) and x[0] != 'bin' for x in v):
            return tuple((k, canon(x, drop)) for k, x in v if k != '$ID' and k not in drop)
        return tuple(canon(x, drop) for x in v)
    if isinstance(v, tuple) and v and v[0] == 'bin': return ('bin',) if len(v[1]) == 16 else v
    return v

def gid(blob): return str(uuid.UUID(bytes_le=bytes(blob))) if blob and len(blob) == 16 else (bytes(blob or b'').hex())

def snapshot(repo, mpr, rev):
    try: raw = subprocess.run(['git', '-C', repo, 'show', f'{rev}:{mpr}'], capture_output=True, check=True).stdout
    except subprocess.CalledProcessError: return None
    f = tempfile.NamedTemporaryFile(suffix='.mpr', delete=False); f.write(raw); f.close()
    c = sqlite3.connect(f'file:{f.name}?mode=ro', uri=True)
    ver = c.execute('select _ProductVersion from _MetaData').fetchone()[0]
    units = {gid(u): (gid(p), h, bytes(b)) for u, p, h, b in c.execute('select UnitID, ContainerID, ContentsHash, Contents from Unit')}
    c.close(); os.unlink(f.name)
    return {'version': ver, 'units': units, 'heads': {}}

def head(s, uid):
    if uid not in s['heads']:
        d = dict(bson_doc(s['units'][uid][2]))
        s['heads'][uid] = (d.get('$Type', ''), d.get('Name', '') if isinstance(d.get('Name'), str) else '')
    return s['heads'][uid]

def module_of(s, uid):
    seen = set(); cur = uid
    while cur and cur not in seen:
        seen.add(cur)
        if cur not in s['units']: return '', ''
        t, n = head(s, cur)
        if t.startswith('Projects$Module') and not t.startswith(('Projects$ModuleSettings', 'Projects$ModuleGuidMapping')): return cur, n
        cur = s['units'][cur][0]
    return '', ''

def changed(p, c, uid):
    a, b = p['units'][uid][2], c['units'][uid][2]
    if a == b: return False
    try:
        da, db = bson_doc(a), bson_doc(b)
        drop = (keys(da, set()) ^ keys(db, set())) if p['version'] != c['version'] else set()
        return canon(da, drop) != canon(db, drop)
    except Exception: return True

def changes(p, c):
    out = []
    def add(s, status, uid):
        t, n = head(s, uid); out.append({'Status': status, 'UnitID': uid, 'UnitType': t, 'UnitName': n, 'Module': module_of(s, uid)[1]})
    p = p or {'version': '', 'units': {}, 'heads': {}}
    upgraded = bool(p['units']) and p['version'] != c['version']
    for uid, (cont, h, _) in c['units'].items():
        if uid not in p['units']: add(c, 'Added', uid)
        elif (p['units'][uid][1] != h or p['units'][uid][2] != c['units'][uid][2]) and changed(p, c, uid): add(c, 'Modified', uid)
        elif p['units'][uid][0] != cont: add(c, 'Moved', uid)
        elif upgraded and cont == uid: add(c, 'Modified', uid)
    for uid in p['units']:
        if uid in c['units']: continue
        mod = module_of(p, uid)[0]
        if mod and mod != uid and mod not in c['units']: continue
        add(p, 'Deleted', uid)
    out.sort(key=lambda x: (x['Module'], x['UnitType'], x['UnitName'], x['UnitID']))
    return out

args = sys.argv[1:]
write = bool(args) and args[0] == '--write'
if write: args = args[1:]
if len(args) < 2: sys.exit(__doc__)
repo, mpr, branch, revs = '.', 'CsvServices.mpr', args[0], args[1:]
for rev in revs:
    c = snapshot(repo, mpr, rev); p = snapshot(repo, mpr, rev + '^')
    note = {'BranchName': '' if branch in ('main', 'master') else branch, 'ModelerVersion': c['version'],
            'ModelChanges': changes(p, c), 'RelatedStories': [], 'SolutionVersion': '',
            'MPRFormatVersion': 'Version1', 'HasModelerVersion': True}
    text = json.dumps(note, separators=(',', ':'), ensure_ascii=False)
    if write:
        subprocess.run(['git', '-C', repo, 'notes', '--ref=mx_metadata', 'add', '-f', '-F', '-', rev], input=text.encode(), check=True)
        print(f'{rev[:10]}: {len(note["ModelChanges"])} model changes')
    else:
        print(rev + '\t' + text)
