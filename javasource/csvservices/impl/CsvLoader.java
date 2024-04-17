package csvservices.impl;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.mendix.core.Core;
import com.mendix.logging.ILogNode;
import com.mendix.systemwideinterfaces.core.IContext;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CsvLoader {
    private static final ILogNode logger = Core.getLogger(CsvServicesImpl.LOG_NORE);

    public static void executeSqlStatement(String sql) {
        logger.info("executeSqlStatement: " + sql);
        Core.dataStorage().executeWithConnection(connection -> {
            String result = null;
            try {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                Boolean status = pstmt.execute();
            } catch (Exception e) {
                logger.error(e.toString(), e);
                throw new RuntimeException(e);
            }
            return result;
        });
    }

    public void executeSqlStatementInputStreamPar(String sql, InputStream is) {
        logger.info("executeSqlStatementInputStreamPar: " + sql);
        Core.dataStorage().executeWithConnection(connection -> {
            String result = null;
            try {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setCharacterStream(1, new InputStreamReader(is));
                Boolean status = pstmt.execute();
            } catch (Exception e) {
                logger.error(e.toString(), e);
                throw new RuntimeException(e);
            }
            return result;
        });
    }

    public void executeSqlStatementInputStreamParJson(String sql, InputStream is) {
        logger.info("executeSqlStatementInputStreamParJson: " + sql);
        Core.dataStorage().executeWithConnection(connection -> {
            String result = null;
            try {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                CsvMapper csvMapper = new CsvMapper();
                // csvMapper.
                pstmt.setCharacterStream(1, new InputStreamReader(is));
                Boolean status = pstmt.execute();
            } catch (Exception e) {
                logger.error(e.toString(), e);
                throw new RuntimeException(e);
            }
            return result;
        });
    }

    public void loadCsvFile(String entity, InputStream is, IContext ctx
    ) {
        String table = Core.getDatabaseTableName(Core.getMetaObject(entity));
        logger.info("loadCsvFile.1 - create table");
        String sql = String.format("create table if not exists %s_ldr (id serial primary key, load_timestamp timestamp default now(), session_id text, user_Id text, transaction_Id text, datafile text)", table);
        executeSqlStatement(sql);

        // importing the zip
        //objectsCreated = Long.valueOf(csvImporter.csvToEntities(getContext(), outputWriter, moduleName, entityName, is, false, maxRecords, this.HasHeader, this.AlternativeHeader, this.Delimiter));
        logger.info("loadCsvFile.2 - insert file");
        String sql2 = String.format("insert into %s_ldr (datafile, session_id, user_Id, transaction_Id) values (?,?,?,?) returning id", table);
        Long id =
                Core.dataStorage().executeWithConnection(connection -> {
                    Long result = null;
                    try {
                        PreparedStatement pstmt = connection.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
                        pstmt.setCharacterStream(1, new InputStreamReader(is));
                        pstmt.setString(2, ctx.getSession().getId().toString());
                        pstmt.setString(3, ctx.getSession().getUserName());
                        pstmt.setString(4, ctx.getTransactionId().toString());
                        Boolean status = pstmt.execute();
                        ResultSet rs = pstmt.getGeneratedKeys();
                        if (rs.next()) {
                            Long _id = rs.getLong(1);
                            logger.info(String.format("Loader row id %d", _id));
                            result = _id;
                        }
                    } catch (Exception e) {
                        logger.error(e.toString(), e);
                        throw new RuntimeException(e);
                    }
                    return result;
                });
        // Get csv headers
        logger.info("loadCsvFile.3 - get headers");
        String sql5 = String.format("with lines as (\n" +
                "    select  split_part(datafile,E'\\n',1) as first_line\n" +
                "    from   %s_ldr" +
                "    where  id = ?\n" +
                ") , cols as (\n" +
                "    select regexp_split_to_array(first_line,',') as col\n" +
                "    from lines\n" +
                ")\n" +
                "select * from cols", table);
        String colNames[] = Core.dataStorage().executeWithConnection(connection -> {
            String[] result = null;
            try {
                PreparedStatement pstmt = connection.prepareStatement(sql5);
                pstmt.setLong(1, id);
                ResultSet rset = pstmt.executeQuery();
                if(rset.next()){
                    result = (String[])rset.getArray(1).getArray();
                }
            } catch (Exception e) {
                logger.error(e.toString(), e);
                throw new RuntimeException(e);
            }
            return result;
        });
        logger.info(String.format("col names: %s",String.join(",",colNames)));
        //
        logger.info("loadCsvFile.4 - drop view");
        String sql3 = String.format("drop view if exists %s_vw cascade", table);
        executeSqlStatement(sql3);
        //
        //String select = Arrays.stream(colNames).map(item -> " col[] as " + item).collect(Collectors.joining(","));
        logger.info("loadCsvFile.5 - create view");
        String select = IntStream
                .range(0,colNames.length)
                .mapToObj(i -> {return String.format("col[%d] as %s",(i+1),colNames[i]);})
                .collect(Collectors.joining(","));
        logger.info(String.format("view cols: %s",select));
        String sql4 = String.format("create or replace view %s_vw as (" +
                "with lines as (\n" +
                "    select id as ldr_id, regexp_split_to_table(datafile,'\\n') as line\n" +
                "    from   %s_ldr\n" +
                ") , cols as (\n" +
                "    select ldr_id, (row_number() over(partition by ldr_id)) - 1 as line_no, regexp_split_to_array(line,',') as col\n" +
                "    from lines\n" +
                ")" +
                "select ldr_id, line_no, %s from cols" +
                ")", table, table, select);
        executeSqlStatement(sql4);
        //
        logger.info("loadCsvFile.6 - drop materialized view");
        String sql6 = String.format("drop materialized view if exists %s_mv cascade",table);
        executeSqlStatement(sql6);
        //
        logger.info("loadCsvFile.7 - create materialized view");
        String sql7 = String.format("create materialized view %s_mv as ( select * from %s_vw)",table,table);
        executeSqlStatement(sql7);
        //
        logger.info("loadCsvFile.8 - refresh materialized view");
        String sql8 = String.format("refresh materialized view %s_mv ",table);
        executeSqlStatement(sql8);
        logger.info("loadCsvFile.9 - done");

    }
}
