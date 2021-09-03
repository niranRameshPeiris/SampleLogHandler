## Steps need to follow 

1. Shutdown APIM 3.1.0 server.
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
4. Start the server.

**OPTIONAL**
<br /> If you need to add these logs into a separate file, you can add a appender by following the below steps. 
1. Add the below content to the Log4j2.properties file.
```
appender.CUSTOM_LOGGER.type = RollingFile
appender.CUSTOM_LOGGER.name = CUSTOM_LOGGER
appender.CUSTOM_LOGGER.fileName = ${sys:carbon.home}/repository/logs/http_gw_passthrough_custom.log
appender.CUSTOM_LOGGER.filePattern = ${sys:carbon.home}/repository/logs/http_gw_passthrough_custom-%d{MM-dd-yyyy}.log
appender.CUSTOM_LOGGER.layout.type = PatternLayout
appender.CUSTOM_LOGGER.layout.pattern = %d{yyyy-MM-dd HH:mm:ss,SSS}||%t|%m%n
appender.CUSTOM_LOGGER.policies.type = Policies
appender.CUSTOM_LOGGER.policies.time.type = TimeBasedTriggeringPolicy
appender.CUSTOM_LOGGER.policies.time.interval = 1
appender.CUSTOM_LOGGER.policies.time.modulate = true
appender.CUSTOM_LOGGER.policies.size.type = SizeBasedTriggeringPolicy
appender.CUSTOM_LOGGER.policies.size.size=10MB
```
2. Append the CUSTOM_LOGGER to the appenders list in the top of the file.
   <br />
```
appenders= ...... ,........ ,CUSTOM_LOGGER
```

3. Engage the custom log handler with the configured appender as below.
```
   logger.CUSTOM_ACCESS_LOG_HANDLER.name=com.wso2.apim.log.handler.SampleLogHandler
   logger.CUSTOM_ACCESS_LOG_HANDLER.level=INFO
   logger.CUSTOM_ACCESS_LOG_HANDLER.appenderRef.CUSTOM_LOGGER.ref = CUSTOM_LOGGER
   logger.CUSTOM_ACCESS_LOG_HANDLER.additivity = false
```

**_Sample output_**
```
[2021-05-17 17:34:02,564]  INFO - SampleLogHandler | API Name : admin--test-api:vv1| API Method : GET| API Request path : /test-context/v1/users| Remote Host : http://localhost:8080| Response status code : 200| Inflow latency : 1| Backend latency : 1330| Outflow latency : 1| Round trip latency : 1332
```
