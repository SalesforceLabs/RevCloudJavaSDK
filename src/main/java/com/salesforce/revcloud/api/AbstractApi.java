package com.salesforce.revcloud.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.salesforce.revcloud.ApiContext;
import com.salesforce.revcloud.api.response.ApiResponse;
import com.salesforce.revcloud.client.ApiClient;
import org.apache.commons.text.StringSubstitutor;
import org.apache.log4j.Logger;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractApi implements ClientSessionChannel.MessageListener {

    ApiResponse apiResponse;
    private String correlationId;
    private Consumer<ApiResponse> messageConsumer;
    private Map<String, String> uriParameters = new HashMap<>();

    private ApiClient apiClient;
    ApiContext apiContext;
    private Object semaphore = new Object();

    private static final Logger LOGGER = Logger.getLogger(AbstractApi.class);

    protected AbstractApi(ApiContext apiContext) {
        if (apiContext == null || !apiContext.isInitiated()) {
            throw new RuntimeException("API context unestablished, call APIContext.init first.");
        }
        this.apiContext = apiContext;
        this.apiClient = new ApiClient();
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public <T extends AbstractApi> T setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return (T)this;
    }

    protected abstract String getEndpoint();

    protected abstract HttpMethod getMethod();

    public <T extends AbstractApi> T setUriParameter(String key, String value) {
        uriParameters.put(key, value);
        return (T)this;
    }

    public String replaceUriParameters(String uriTemplate) {
        StringSubstitutor substitutor = new StringSubstitutor(this.uriParameters, "{", "}");
        return substitutor.replace(uriTemplate);
    }

    protected String getPayload() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return "{\"inputs\":[" + mapper.writeValueAsString(this) + "]}";
    }

    protected abstract String getEventType();

    protected abstract ApiResponse createApiResponse(String action, boolean isSuccess, List<Object> errors, String requestId, Map<String, Object> outputValues);

    protected abstract void onMessage(Message message);

    public <T extends AbstractApi> T setMessageConsumer(Consumer<ApiResponse> messageConsumer) {
        if (getEventType() == null) {
            throw new UnsupportedOperationException("Async message consumption is not supported for this API");
        }
        this.messageConsumer = messageConsumer;
        return (T)this;
    }

    public <T extends ApiResponse> T getApiResponse() {
        return (T)this.apiResponse;
    }

    void setApiResponse(ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }

    public <T extends ApiResponse> T invoke() throws Exception {
        if (getEventType() != null) {
            apiContext.subscribe(getEventType().toString(), this);
        }
        ContentResponse response = apiClient.invokeApi(apiContext.buildFullApiUrl(getEndpoint()), apiContext.getSessionId(), getMethod(), getPayload());
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return processResult(response, mapper);
    }

    <T extends ApiResponse> T processResult(ContentResponse response, ObjectMapper mapper) throws JsonProcessingException {
        String requestId = null;
        boolean isSuccess = false;
        List errors;
        byte[] result = response.getContent();
        List dataList = mapper.readValue(new String(result), List.class);
        Map<String, Object> outputValues = Collections.EMPTY_MAP;
        if (dataList.size() > 0) {
            Map<String, Object> data = (Map<String, Object>)dataList.get(0);
            isSuccess = data.get("isSuccess") != null && (Boolean)data.get("isSuccess");
            LOGGER.info("API invocation successful: " + isSuccess);
            LOGGER.debug("API response: " + new String(result));
            outputValues = ((Map<String, Object>)data.get("outputValues"));
            if (outputValues != null) {
                requestId = (String)outputValues.get("requestId");
            } else {
                outputValues = new HashMap<>();
            }
            if (data.containsKey("errorCode")) {
                errors = dataList;
            } else {
                errors = (List) data.get("errors");
            }
        } else {
            errors = ImmutableList.of("Received malformed data: " + new String(result));
        }
        apiResponse = createApiResponse(this.getClass().getSimpleName(), isSuccess, errors, requestId, outputValues);
        return (T)apiResponse;
    }

    public <T extends ApiResponse> T invokeAndWait() throws Exception {
        apiResponse = invoke();
        if (apiResponse.isSuccess() && getEventType() != null) {
            long now = System.currentTimeMillis();
            synchronized (semaphore) {
                semaphore.wait(apiContext.getEventListenerTimeout());
            }
            if (!apiResponse.isAsyncEventReceived()) {
                String message = getEventType() + " listener timed out after " + (System.currentTimeMillis() - now) + "ms";
                if (apiContext.isThrowOnEventTimeout()) {
                    throw new RuntimeException(message);
                } else {
                    LOGGER.error(message);
                }
            } else {
                LOGGER.debug(getEventType() + " received in " + (System.currentTimeMillis() - now) + "ms");
            }
        }
        return (T)apiResponse;
    }

    public Map<String, Object> getMessagePayload(Message message) {
        Map<String, Object> data = (Map<String, Object>)message.getData();
        return (Map<String, Object>)data.get("payload");
    }

    @Override
    public void onMessage(ClientSessionChannel clientSessionChannel, Message message) {
        Map<String, Object> payload = getMessagePayload(message);
        if ((apiResponse.getRequestId() != null && apiResponse.getRequestId().equals(payload.get("RequestIdentifier"))) ||
                (correlationId != null && correlationId.equals(payload.get("CorrelationIdentifier")))) {
            apiResponse.setCorrelationId((String)payload.get("CorrelationIdentifier"));
            apiResponse.setAsyncEventReceived(true);
            LOGGER.debug("Got matching event for API invocation for request: " + payload.get("RequestIdentifier"));
            // event success flag
            if (Boolean.TRUE.equals(payload.get("HasErrors")) || Boolean.FALSE.equals(payload.get("IsSuccess"))) {
                apiResponse.setSuccess(false);
            }
            onMessage(message);
            if (this.messageConsumer != null) {
                try {
                    messageConsumer.accept(getApiResponse());
                } catch (Throwable e) {
                    LOGGER.error("Error invoking message consumer", e);
                }
            }
            synchronized (semaphore) {
                semaphore.notify();
            }
        } else {
            LOGGER.warn("Got message but not matching request id: " + apiResponse.getRequestId());
        }
    }

    protected SObject deserializeSObject(Object value) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper.convertValue(value, SObject.class);
    }


}
