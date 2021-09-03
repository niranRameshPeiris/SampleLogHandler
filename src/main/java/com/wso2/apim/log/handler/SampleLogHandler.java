package com.wso2.apim.log.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.AbstractSynapseHandler;
import org.apache.synapse.MessageContext;

public class SampleLogHandler extends AbstractSynapseHandler {
    Log log = LogFactory.getLog(SampleLogHandler.class);

    public SampleLogHandler() {

    }

    /**
     * @param messageContext synapse messageContext
     * @return requext Inflow time
     */
    public boolean handleRequestInFlow(MessageContext messageContext) {
        messageContext.setProperty(LogConstants.PROPERTY_REQUEST_EXECUTION_START_TIME, Long.toString(System.currentTimeMillis()));
        String apiMethod = LogUtils.getInstance().getRestMethod(messageContext);
        messageContext.setProperty(LogConstants.API_METHOD, apiMethod);
        return true;
    }

    /**
     * @param messageContext synapse messageContext
     * @return request outflow time
     */
    public boolean handleRequestOutFlow(MessageContext messageContext) {
        try {
            messageContext.setProperty(LogConstants.PROPERTY_BACKEND_REQUEST_TIME, Long.toString(System.currentTimeMillis()));
            String apiTo = LogUtils.getInstance().getTo(messageContext);
            messageContext.setProperty(LogConstants.BACKEND_URL, apiTo);
            return true;
        } catch (Exception e) {
            log.error("Cannot publish request event.", e);
        }
        return true;
    }

    /**
     * @param messageContext synapse messageContext
     * @return response Inflow time
     */
    public boolean handleResponseInFlow(MessageContext messageContext) {
        try {
            messageContext.setProperty(LogConstants.PROPERTY_BACKEND_REQUEST_END_TIME, Long.toString(System.currentTimeMillis()));
            return true;
        } catch (Exception e) {
            log.error("Cannot publish response event.", e);
        }
        return true;
    }

    /**
     * @param messageContext synapse messageContext
     * @return response outflow time
     */
    public boolean handleResponseOutFlow(MessageContext messageContext) {
        try {
            long requestInTime = 0;
            long requestOutTime = 0;
            long responseInTime = 0;

            String inflowLatency = LogConstants.DEFAULT_STRING, outflowLatency = LogConstants.DEFAULT_STRING, backendLatency = LogConstants.DEFAULT_STRING, roundTripLatency = LogConstants.DEFAULT_STRING;

            String apiMethod = (String) messageContext.getProperty(LogConstants.API_METHOD);
            String apiName = LogUtils.getInstance().getAPIName(messageContext);
            String apiTo = (String) messageContext.getProperty(LogConstants.BACKEND_URL);
            String apiFullRequestPath = (String) messageContext.getProperty(LogConstants.REST_FULL_REQUEST_PATH);
            int apiResponseSC = LogUtils.getInstance().getIntRestHttpResponseStatusCode(messageContext);

            String requestInTimeValue = (String) messageContext.getProperty(LogConstants.PROPERTY_REQUEST_EXECUTION_START_TIME);
            String requestOutTimeValue = (String) messageContext.getProperty(LogConstants.PROPERTY_BACKEND_REQUEST_TIME);
            String responseInTimeValue = (String) messageContext.getProperty(LogConstants.PROPERTY_BACKEND_REQUEST_END_TIME);

            if (requestInTimeValue != null && !requestInTimeValue.isEmpty()) {
                requestInTime = Long.parseLong(requestInTimeValue);
            }
            if (requestOutTimeValue != null && !requestOutTimeValue.isEmpty()) {
                requestOutTime = Long.parseLong(requestOutTimeValue);
            }
            if (responseInTimeValue != null && !responseInTimeValue.isEmpty()) {
                responseInTime = Long.parseLong(responseInTimeValue);
            }

            if (requestInTime <= requestOutTime && requestInTime != 0) {
                inflowLatency = Long.toString(requestOutTime - requestInTime);
            }
            if (responseInTime <= System.currentTimeMillis() && responseInTime != 0) {
                outflowLatency = Long.toString(System.currentTimeMillis() - responseInTime);
            }
            if (requestOutTime <= responseInTime && requestOutTime != 0) {
                backendLatency = Long.toString(responseInTime - requestOutTime);
            }
            if (requestInTime <= System.currentTimeMillis() && requestInTime != 0) {
                roundTripLatency = Long.toString(System.currentTimeMillis() - requestInTime);
            }

            if (apiResponseSC >= 500 && apiResponseSC < 600) {
                log.info("| API Name : " + apiName + "| API Method : " + apiMethod + "| API Request path : " + apiFullRequestPath + "| Remote Host : "
                        + apiTo + "| Response status code : " + apiResponseSC + "| Inflow latency : " + inflowLatency + "| Backend latency : " + backendLatency
                        + "| Outflow latency : " + outflowLatency + "| Round trip latency : " + roundTripLatency);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return true;
    }
}
