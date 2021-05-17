## Steps need to follow 

1. Shutdown APIM 3.2.0 server.
2. Open [APIT_HOME]/repository/conf/deployment.toml file and add the below configuration at the very top of the file. (Even before [server] tag)

```
enabled_global_handlers= ["SampleLogHandler","externalCallLogger","open_tracing"]
[synapse_handlers]
SampleLogHandler.name= "SampleLogHandler"
SampleLogHandler.class= "com.wso2.apim.log.handler.SampleLogHandler"
externalCallLogger.name= "externalCallLogger"
externalCallLogger.class= "org.wso2.carbon.apimgt.gateway.handlers.LogsHandler"
open_tracing.name= "open_tracing"
open_tracing.class= "org.wso2.carbon.apimgt.gateway.handlers.common.APIMgtLatencySynapseHandler"
```

3. Open [APIT_HOME]/repository/conf/log4j2.properties file and add the below configs to it.

```
logger.com.wso2.apim.log.handler.name=com.wso2.apim.log.handler.SampleLogHandler
logger.com.wso2.apim.log.handler.level=INFO
logger.com.wso2.apim.log.handler.appenderRef.CARBON_LOGFILE.ref = CARBON_LOGFILE
```
In the log4j2.properties file add the new logger name to the loggers.
loggers = <Existing Loggers>, com.wso2.apim.log.handler

4. Restart the server.

5. Sample output.
```
[2021-05-17 17:34:02,564]  INFO - SampleLogHandler | API Name : admin--test-api:vv1| API Method : GET| API Request path : /test-context/v1/users| Remote Host : http://localhost:8080| Response status code : 200| Inflow latency : 1| Backend latency : 1330| Outflow latency : 1| Round trip latency : 1332
```
