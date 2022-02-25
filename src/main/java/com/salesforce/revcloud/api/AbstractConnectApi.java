package com.salesforce.revcloud.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.revcloud.ApiContext;
import com.salesforce.revcloud.api.response.ApiResponse;
import org.apache.commons.text.StringSubstitutor;
import org.eclipse.jetty.client.api.ContentResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractConnectApi extends AbstractApi {

    private Object input;

    protected AbstractConnectApi(ApiContext apiContext) {
        super(apiContext);
    }

    public Object getInput() {
        return input;
    }

    public <T extends AbstractConnectApi> T setInput(Object input) {
        this.input = input;
        return (T)this;
    }

    protected String getPayload() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(getInput());
    }

    @Override
    <T extends ApiResponse> T processResult(ContentResponse response, ObjectMapper mapper) throws JsonProcessingException {
        ApiResponse apiResponse;
        byte[] result = response.getContent();
        if (response.getStatus() == 400) {
            List errors = mapper.readValue(new String(result), List.class);
            apiResponse = createApiResponse(this.getClass().getSimpleName(), false, errors, null, null);
        } else {
            Map outputValues = mapper.readValue(new String(result), Map.class);
            boolean isSuccess = response.getStatus() >= 200 && response.getStatus() <= 299;
            apiResponse = createApiResponse(this.getClass().getSimpleName(), isSuccess, null, null, outputValues);
        }
        return (T)apiResponse;
    }
}
