set NETCAT_PATH=c:\programs\netcat\
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_92\bin
set APP_HOME=C:\Users\ako\Dropbox (Mendix)\Projects\mendix\CsvServices
set APP_PATH=C:/Users/ako/Dropbox (Mendix)/Projects/mendix/CsvServices
set MX_INSTALL_PATH=C:\Program Files\Mendix\7-build13078
set MX_INSTALL_PATH_2=C:/Program Files/Mendix/7-build13078
set M2EE_ADMIN_PASS=1
set M2EE_ADMIN_PORT=8090
set APP_LOG_PATH=\temp\app.log
set APP_LOG_PATH_UX=/temp/app.log

rem
rem start mendix runtime launcher
rem

start /b "runtime" "%JAVA_HOME%\java.exe" -Djava.net.preferIPv4Stack=true -DMX_LOG_LEVEL=INFO -Djava.library.path="%MX_INSTALL_PATH%/runtime/lib/x64;%APP_HOME%/deployment/model/lib/userlib" -Dfile.encoding=UTF-8 -Djava.io.tmpdir="%APP_HOME%/deployment/data/tmp" -Djava.security.manager -Djava.security.policy=="%APP_HOME%\deployment\data\.policy" -jar "%MX_INSTALL_PATH%\runtime\launcher\runtimelauncher.jar" "%APP_HOME%\deployment" 

sleep 5

rem
rem create logfile subscriber
rem


set CMD='{"action": "create_log_subscriber", "params": {"max_rotation": 7, "name": "MXLOGSUBS", "filename": "%APP_LOG_PATH_MX%", "autosubscribe": "INFO", "type": "file", "max_size": 10485760}}'
curl -i -H "X-M2EE-Authentication: MQ==" -H "Content-Type: application/json" -X POST http://localhost:8090/ -d %CMD%

rem
rem start tcp logsubscriber
rem

rem start /b "netcat" cmd /c "%NETCAT_PATH%\nc.exe -l -p 31337 | jq '.'"

rem set CMD="{'action': 'create_log_subscriber', 'params': {'type': 'tcpjson', 'name': 'TCPJSONLog','autosubscribe': 'INFO', 'host': '127.0.0.1', 'port':31337}}" 
rem curl -i -H "X-M2EE-Authentication: MQ==" -H "Content-Type: application/json" -X POST http://localhost:8090/ -d %CMD%

rem
rem start runtime logging
rem

set CMD="{'action': 'start_logging'}" 
curl -i -H "X-M2EE-Authentication: MQ==" -H "Content-Type: application/json" -X POST http://localhost:8090/ -d %CMD%

rem
rem set loglevels
rem

set CMD="{ 'action':'set_log_level', 'params': {'nodes' : [ { 'name':'ConnectionBus', 'level':'INFO'}, { 'name':'ActionManager', 'level':'INFO'} ], 'force':True} }"
curl -i -H "X-M2EE-Authentication: MQ==" -H "Content-Type: application/json" -X POST http://localhost:8090/ -d %CMD%

rem
rem configure appcontainer
rem

set CMD="{'action': 'update_appcontainer_configuration', 'params': {'runtime_port': 8000, 'runtime_listen_addresses': '*', 'runtime_jetty_options': {'use_blocking_connector': false}}}"
curl -i -H "X-M2EE-Authentication: MQ==" -H "Content-Type: application/json" -X POST http://localhost:8090/ -d %CMD%

rem
rem update configuration
rem

set CMD="{'action': 'update_configuration', 'params': {'DatabaseHost': '127.0.0.1:5432', 'DTAPMode': 'D', 'MicroflowConstants': {'CsvServices.CSV_SERVICES_VERSION': '1.1.3'}, 'BasePath': '%APP_PATH%/Deployment', 'DatabaseUserName': 'mx', 'DatabasePassword': 'mx', 'DatabaseName': 'mxdev3', 'RuntimePath': '%MX_INSTALL_PATH_2%/runtime', 'DatabaseType': 'PostgreSQL', 'ScheduledEventExecution': 'NONE'}}"
curl -i -H "X-M2EE-Authentication: MQ==" -H "Content-Type: application/json" -X POST http://localhost:8090/ -d %CMD%

rem
rem start app
rem

set CMD="{'action': 'start'}"
curl -i -H "X-M2EE-Authentication: MQ==" -H "Content-Type: application/json" -X POST http://localhost:8090/ -d %CMD%

tail -f %APP_LOG_PATH%
