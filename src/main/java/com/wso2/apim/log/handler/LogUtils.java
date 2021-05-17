package com.wso2.apim.log.handler;

import org.apache.synapse.core.axis2.Axis2MessageContext;

public class LogUtils {
    LogUtils() {}


    protected static String getAPIName(org.apache.synapse.MessageContext messageContext) {
        return (String)messageContext.getProperty("SYNAPSE_REST_API");
    }

    protected static String getRestMethod(org.apache.synapse.MessageContext messageContext) {
        org.apache.axis2.context.MessageContext axis2MsgContext = ((Axis2MessageContext)messageContext)
                .getAxis2MessageContext();
        return (String)axis2MsgContext.getProperty("HTTP_METHOD");
    }

    protected static String getRestHttpResponseStatusCode(org.apache.synapse.MessageContext messageContext) {
        org.apache.axis2.context.MessageContext axis2MsgContext = ((Axis2MessageContext)messageContext)
                .getAxis2MessageContext();
        return String.valueOf(axis2MsgContext.getProperty("HTTP_SC"));
    }

    protected static String getTo(org.apache.synapse.MessageContext messageContext) {
        return (String)messageContext.getProperty("DYNAMIC_URL_VALUE");
    }
}
