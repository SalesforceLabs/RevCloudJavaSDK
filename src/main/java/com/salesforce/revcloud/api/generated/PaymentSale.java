/**
 * PaymentSale.java
 *
 * API client for PaymentSale
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
import com.salesforce.revcloud.api.generated.response.PaymentSaleResponse;
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
public class PaymentSale extends AbstractApi {

    public PaymentSale(ApiContext apiContext) {
        super(apiContext);
    }

    private Double amount;
    private String paymentGatewayId;
    private String paymentMethodId;
    private String errorLogPrimaryRecordId;
    private String currencyIsoCode;
    private String idempotencyKey;

    public Double getAmount() {
        return this.amount;
    }

    public PaymentSale setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public String getPaymentGatewayId() {
        return this.paymentGatewayId;
    }

    public PaymentSale setPaymentGatewayId(String paymentGatewayId) {
        this.paymentGatewayId = paymentGatewayId;
        return this;
    }

    public String getPaymentMethodId() {
        return this.paymentMethodId;
    }

    public PaymentSale setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public String getErrorLogPrimaryRecordId() {
        return this.errorLogPrimaryRecordId;
    }

    public PaymentSale setErrorLogPrimaryRecordId(String errorLogPrimaryRecordId) {
        this.errorLogPrimaryRecordId = errorLogPrimaryRecordId;
        return this;
    }

    public String getCurrencyIsoCode() {
        return this.currencyIsoCode;
    }

    public PaymentSale setCurrencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
        return this;
    }

    public String getIdempotencyKey() {
        return this.idempotencyKey;
    }

    public PaymentSale setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
        return this;
    }

    @Override
    protected String getEndpoint() {
        return replaceUriParameters("/actions/standard/paymentSale");
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected String getEventType() {
        return "PaymentCreationEvent";
    }

    @Override
    protected ApiResponse createApiResponse(String action, boolean isSuccess, List<Object> errors, String requestId, Map<String, Object> outputValues) {
        PaymentSaleResponse response = new PaymentSaleResponse(action, isSuccess, errors, requestId);
        response.setApiOutput(outputValues);
        response.setRequestId((String)outputValues.get("requestId"));
        return response;
    }

    @Override
    protected void onMessage(Message message) {
        Map<String, Object> payload = getMessagePayload(message);
        PaymentSaleResponse apiResponse = getApiResponse();
        PaymentCreationEvent paymentCreationEvent = new PaymentCreationEvent();
        paymentCreationEvent.setCreatedDate((String)payload.get("CreatedDate"));
        paymentCreationEvent.setCreatedById((String)payload.get("CreatedById"));
        paymentCreationEvent.setRequestIdentifier((String)payload.get("RequestIdentifier"));
        paymentCreationEvent.setCorrelationIdentifier((String)payload.get("CorrelationIdentifier"));
        paymentCreationEvent.setPaymentId((String)payload.get("PaymentId"));
        paymentCreationEvent.setType((String)payload.get("Type"));
        paymentCreationEvent.setIsSuccess((Boolean)payload.get("IsSuccess"));
        paymentCreationEvent.setPaymentStatus((String)payload.get("PaymentStatus"));
        paymentCreationEvent.setPaymentGatewayLogId((String)payload.get("PaymentGatewayLogId"));
        paymentCreationEvent.setErrorCode((String)payload.get("ErrorCode"));
        paymentCreationEvent.setErrorMessage((String)payload.get("ErrorMessage"));

        apiResponse.setPaymentCreationEvent(paymentCreationEvent);
    }
}
