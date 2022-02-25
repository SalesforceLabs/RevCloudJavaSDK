/**
 * CreateBillingScheduleFromOrderItem.java
 *
 * API client for CreateBillingScheduleFromOrderItem
 * Do NOT edit as this is generated by APIGenerator
 */
package com.salesforce.revcloud.api.generated;

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
import com.salesforce.revcloud.api.generated.response.CreateBillingScheduleFromOrderItemResponse;
import com.salesforce.revcloud.api.generated.event.*;

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
public class CreateBillingScheduleFromOrderItem extends AbstractApi {

    public CreateBillingScheduleFromOrderItem(ApiContext apiContext) {
        super(apiContext);
    }

    private List<String> orderItemIds;
    private String correlationId;

    public List<String> getOrderItemIds() {
        return this.orderItemIds;
    }

    public CreateBillingScheduleFromOrderItem setOrderItemIds(List<String> orderItemIds) {
        this.orderItemIds = orderItemIds;
        return this;
    }

    public String getCorrelationId() {
        return this.correlationId;
    }

    public CreateBillingScheduleFromOrderItem setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    @Override
    protected String getEndpoint() {
        return replaceUriParameters("/actions/standard/createBillingScheduleFromOrderItem");
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected String getEventType() {
        return "BillingScheduleCreatedEvent";
    }

    @Override
    protected ApiResponse createApiResponse(String action, boolean isSuccess, List<Object> errors, String requestId, Map<String, Object> outputValues) {
        CreateBillingScheduleFromOrderItemResponse response = new CreateBillingScheduleFromOrderItemResponse(action, isSuccess, errors, requestId);
        response.setApiOutput(outputValues);
        response.setRequestId((String)outputValues.get("requestId"));
        return response;
    }

    @Override
    protected void onMessage(Message message) {
        Map<String, Object> payload = getMessagePayload(message);
        CreateBillingScheduleFromOrderItemResponse apiResponse = getApiResponse();
        BillingScheduleCreatedEvent billingScheduleCreatedEvent = new BillingScheduleCreatedEvent();
        billingScheduleCreatedEvent.setCreatedDate((String)payload.get("CreatedDate"));
        billingScheduleCreatedEvent.setCreatedById((String)payload.get("CreatedById"));
        billingScheduleCreatedEvent.setRequestIdentifier((String)payload.get("RequestIdentifier"));
        billingScheduleCreatedEvent.setCorrelationIdentifier((String)payload.get("CorrelationIdentifier"));
        Object[] billingScheduleCreatedEventDetailArray = (Object[])payload.get("BillingScheduleCreatedEventDetail");
        if (billingScheduleCreatedEventDetailArray != null) {
            for (Object billingScheduleCreatedEventDetailObj : billingScheduleCreatedEventDetailArray) {
                Map<String, Object> billingScheduleCreatedEventDetailMap = (Map<String, Object>)billingScheduleCreatedEventDetailObj;
                BillSchdCreatedEventDetail billSchdCreatedEventDetail = new BillSchdCreatedEventDetail();
        billSchdCreatedEventDetail.setOrderItemId((String)billingScheduleCreatedEventDetailMap.get("OrderItemId"));
        billSchdCreatedEventDetail.setBillingScheduleId((String)billingScheduleCreatedEventDetailMap.get("BillingScheduleId"));
        billSchdCreatedEventDetail.setIsSuccess((Boolean)billingScheduleCreatedEventDetailMap.get("IsSuccess"));
        billSchdCreatedEventDetail.setErrorCode((String)billingScheduleCreatedEventDetailMap.get("ErrorCode"));
        billSchdCreatedEventDetail.setErrorMessage((String)billingScheduleCreatedEventDetailMap.get("ErrorMessage"));

                billingScheduleCreatedEvent.getBillingScheduleCreatedEventDetails().add(billSchdCreatedEventDetail);
            }
        }

        apiResponse.setBillingScheduleCreatedEvent(billingScheduleCreatedEvent);
    }
}
