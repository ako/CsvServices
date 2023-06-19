set CMD="{ 'action':'set_log_level', 'params': {'nodes' : [ { 'name':'RequestStatistics', 'level':'TRACE'}], 'force':True} }"
curl -i -H "X-M2EE-Authentication: MQ==" -H "Content-Type: application/json" -X POST http://localhost:8092/ -d %CMD%
curl -i -H "X-M2EE-Authentication: MQ==" -H "Content-Type: application/json" -X POST http://localhost:8093/ -d %CMD%
