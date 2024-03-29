/**
 * CalculatePrice.java
 *
 * API client for CalculatePrice
 * Do NOT edit as this is generated by APIGenerator
 */
package com.salesforce.revcloud.api.generated.commerce.pricing;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.revcloud.ApiContext;
import com.salesforce.revcloud.api.*;
import com.salesforce.revcloud.api.response.ApiResponse;
import com.salesforce.revcloud.api.serializer.DateSerializer;
import com.salesforce.revcloud.api.SObject;
import org.cometd.bayeux.Message;
import org.eclipse.jetty.http.HttpMethod;
import com.salesforce.revcloud.api.generated.commerce.pricing.response.CalculatePriceResponse;
import com.salesforce.revcloud.api.generated.commerce.pricing.type.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author kzheng
 * @since 1/5/2021
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalculatePrice extends AbstractConnectApi {

    public CalculatePrice(ApiContext apiContext) {
        super(apiContext);
    }


    @Override
    protected String getEndpoint() {
        return replaceUriParameters("/commerce/pricing/{sobject}/actions/calculate-price");
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected String getEventType() {
        return null;
    }

    @Override
    protected ApiResponse createApiResponse(String action, boolean isSuccess, List<Object> errors, String requestId, Map<String, Object> outputValues) {
        CalculatePriceResponse response = new CalculatePriceResponse(action, isSuccess, errors, requestId);
        response.setApiOutput(outputValues);
        return response;
    }

    @Override
    protected void onMessage(Message message) {
    }
}
