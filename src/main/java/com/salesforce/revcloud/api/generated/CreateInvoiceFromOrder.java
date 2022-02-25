/**
 * CreateInvoiceFromOrder.java
 *
 * API client for CreateInvoiceFromOrder
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
import com.salesforce.revcloud.api.generated.response.CreateInvoiceFromOrderResponse;
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
public class CreateInvoiceFromOrder extends AbstractApi {

    public CreateInvoiceFromOrder(ApiContext apiContext) {
        super(apiContext);
    }

    private String orderId;
    private Date invoiceDate;
    private Date targetDate;
    private String invoiceStatus;
    private String correlationId;

    public String getOrderId() {
        return this.orderId;
    }

    public CreateInvoiceFromOrder setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    @JsonSerialize(using = DateSerializer.class)
    public Date getInvoiceDate() {
        return this.invoiceDate;
    }

    public CreateInvoiceFromOrder setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    @JsonSerialize(using = DateSerializer.class)
    public Date getTargetDate() {
        return this.targetDate;
    }

    public CreateInvoiceFromOrder setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
        return this;
    }

    public String getInvoiceStatus() {
        return this.invoiceStatus;
    }

    public CreateInvoiceFromOrder setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
        return this;
    }

    public String getCorrelationId() {
        return this.correlationId;
    }

    public CreateInvoiceFromOrder setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    @Override
    protected String getEndpoint() {
        return replaceUriParameters("/actions/standard/createInvoiceFromOrder");
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected String getEventType() {
        return "InvoiceProcessedEvent";
    }

    @Override
    protected ApiResponse createApiResponse(String action, boolean isSuccess, List<Object> errors, String requestId, Map<String, Object> outputValues) {
        CreateInvoiceFromOrderResponse response = new CreateInvoiceFromOrderResponse(action, isSuccess, errors, requestId);
        response.setApiOutput(outputValues);
        response.setRequestId((String)outputValues.get("requestId"));
        return response;
    }

    @Override
    protected void onMessage(Message message) {
        Map<String, Object> payload = getMessagePayload(message);
        CreateInvoiceFromOrderResponse apiResponse = getApiResponse();
        InvoiceProcessedEvent invoiceProcessedEvent = new InvoiceProcessedEvent();
        invoiceProcessedEvent.setCreatedDate((String)payload.get("CreatedDate"));
        invoiceProcessedEvent.setCreatedById((String)payload.get("CreatedById"));
        invoiceProcessedEvent.setRequestIdentifier((String)payload.get("RequestIdentifier"));
        invoiceProcessedEvent.setCorrelationIdentifier((String)payload.get("CorrelationIdentifier"));
        Object[] invoiceProcessedDetailEventsArray = (Object[])payload.get("InvoiceProcessedDetailEvents");
        if (invoiceProcessedDetailEventsArray != null) {
            for (Object invoiceProcessedDetailEventsObj : invoiceProcessedDetailEventsArray) {
                Map<String, Object> invoiceProcessedDetailEventsMap = (Map<String, Object>)invoiceProcessedDetailEventsObj;
                InvoiceProcessedDetailEvent invoiceProcessedDetailEvent = new InvoiceProcessedDetailEvent();
        Object[] invoiceErrorDetailEventsArray = (Object[])invoiceProcessedDetailEventsMap.get("InvoiceErrorDetailEvents");
        if (invoiceErrorDetailEventsArray != null) {
            for (Object invoiceErrorDetailEventsObj : invoiceErrorDetailEventsArray) {
                Map<String, Object> invoiceErrorDetailEventsMap = (Map<String, Object>)invoiceErrorDetailEventsObj;
                InvoiceErrorDetailEvent invoiceErrorDetailEvent = new InvoiceErrorDetailEvent();
        invoiceErrorDetailEvent.setErrorSourceId((String)invoiceErrorDetailEventsMap.get("ErrorSourceId"));
        invoiceErrorDetailEvent.setErrorCode((String)invoiceErrorDetailEventsMap.get("ErrorCode"));
        invoiceErrorDetailEvent.setErrorMessage((String)invoiceErrorDetailEventsMap.get("ErrorMessage"));

                invoiceProcessedDetailEvent.getInvoiceErrorDetailEvents().add(invoiceErrorDetailEvent);
            }
        }
        invoiceProcessedDetailEvent.setInvoiceId((String)invoiceProcessedDetailEventsMap.get("InvoiceId"));
        invoiceProcessedDetailEvent.setInvoiceStatus((String)invoiceProcessedDetailEventsMap.get("InvoiceStatus"));
        invoiceProcessedDetailEvent.setIsSuccess((Boolean)invoiceProcessedDetailEventsMap.get("IsSuccess"));

                invoiceProcessedEvent.getInvoiceProcessedDetailEvents().add(invoiceProcessedDetailEvent);
            }
        }
        invoiceProcessedEvent.setIsSuccess((Boolean)payload.get("IsSuccess"));
Map<String, Object> invoiceErrorDetailEventMap = (Map<String, Object>)payload.get("InvoiceErrorDetailEvent");
if (invoiceErrorDetailEventMap != null) {
    InvoiceErrorDetailEvent invoiceErrorDetailEvent = new InvoiceErrorDetailEvent();
        invoiceErrorDetailEvent.setErrorSourceId((String)invoiceErrorDetailEventMap.get("ErrorSourceId"));
        invoiceErrorDetailEvent.setErrorCode((String)invoiceErrorDetailEventMap.get("ErrorCode"));
        invoiceErrorDetailEvent.setErrorMessage((String)invoiceErrorDetailEventMap.get("ErrorMessage"));

    invoiceProcessedEvent.setInvoiceErrorDetailEvent(invoiceErrorDetailEvent);
}

        apiResponse.setInvoiceProcessedEvent(invoiceProcessedEvent);
    }
}