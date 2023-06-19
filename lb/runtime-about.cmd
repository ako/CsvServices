set M2EE_ADMIN_PASS=2
set M2EE_ADMIN_PASS_BASE64=Mg0K

set CMD="{ 'action':'set_log_level', 'params': {'nodes' : [ { 'name':'RequestStatistics', 'level':'TRACE'}], 'force':True} }"
curl -i -H "X-M2EE-Authentication: %M2EE_ADMIN_PASS_BASE64%" -H "Content-Type: application/json" -X POST http://localhost:8092/ -d %CMD%
