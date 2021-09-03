package com.wso2.apim.log.handler;

import org.apache.synapse.core.axis2.Axis2MessageContext;

public class LogUtils {
    private static LogUtils instance;

    public static LogUtils getInstance() {
        if (instance == null) {
            instance = new LogUtils();
        }
        return instance;
    }

    protected String getAPIName(org.apache.synapse.MessageContext messageContext) {
        return (String) messageContext.getProperty(LogConstants.SYNAPSE_REST_API);
    }

    protected String getRestMethod(org.apache.synapse.MessageContext messageContext) {
        org.apache.axis2.context.MessageContext axis2MsgContext = ((Axis2MessageContext) messageContext)
                .getAxis2MessageContext();
        return (String) axis2MsgContext.getProperty(LogConstants.HTTP_METHOD);
    }

    protected String getRestHttpResponseStatusCode(org.apache.synapse.MessageContext messageContext) {
        org.apache.axis2.context.MessageContext axis2MsgContext = ((Axis2MessageContext) messageContext)
                .getAxis2MessageContext();
        return String.valueOf(axis2MsgContext.getProperty(LogConstants.HTTP_SC));
    }

    protected int getIntRestHttpResponseStatusCode(org.apache.synapse.MessageContext messageContext) throws NumberFormatException {
        org.apache.axis2.context.MessageContext axis2MsgContext = ((Axis2MessageContext) messageContext)
                .getAxis2MessageContext();
        return Integer.parseInt(String.valueOf(axis2MsgContext.getProperty(LogConstants.HTTP_SC)).trim());
    }

    protected String getTo(org.apache.synapse.MessageContext messageContext) {
        return (String) messageContext.getProperty(LogConstants.DYNAMIC_URL_VALUE);
    }
}
