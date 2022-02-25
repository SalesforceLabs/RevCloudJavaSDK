package com.salesforce.revcloud.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.revcloud.ApiContext;
import com.salesforce.revcloud.api.response.ApiResponse;
import com.salesforce.revcloud.api.response.SoqlQueryResponse;
import org.cometd.bayeux.Message;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class SoqlQuery extends AbstractApi {

    private String query;

    public SoqlQuery(ApiContext apiContext) {
        super(apiContext);
    }

    public SoqlQueryResponse execute(String query) throws Exception {
        this.query = query;
        return (SoqlQueryResponse)super.invoke();
    }

    @Override
    protected String getEndpoint() {
        return "/query?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected String getEventType() {
        return null;
    }

    @Override
    protected ApiResponse createApiResponse(String action, boolean isSuccess, List<Object> errors, String requestId, Map<String, Object> outputValues) {
        return new SoqlQueryResponse(action, isSuccess, errors, requestId);
    }

    @Override
    protected void onMessage(Message message) {
        throw new UnsupportedOperationException("onMessage is not supported");
    }

    @Override
    public <T extends ApiResponse> T invoke() {
        throw new UnsupportedOperationException("invoke is not supported, call execute instead");
    }

    @Override
    <T extends ApiResponse> T processResult(ContentResponse contentResponse, ObjectMapper mapper) throws JsonProcessingException {
        setApiResponse(createApiResponse("SoqlQuery", true, null, null, null));
        Map<String, Object> data = mapper.readValue(new String(contentResponse.getContent()), Map.class);
        SoqlQueryResponse apiResponse = getApiResponse();
        apiResponse.setTotalSize((Integer)data.get("totalSize"));
        apiResponse.setDone((Boolean)data.get("done"));
        apiResponse.setRecords((List<Map<String, Object>>)data.get("records"));
        return (T)apiResponse;
    }
}
