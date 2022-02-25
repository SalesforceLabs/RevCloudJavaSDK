/**
 * BillSchdCreatedEventDetail.java
 *
 * Event class for BillSchdCreatedEventDetail
 * Do NOT edit as this is generated by APIGenerator
 */
package com.salesforce.revcloud.api.generated.event;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * @author kzheng
 * @since 1/5/2021
 */
public class BillSchdCreatedEventDetail {

    private String orderItemId;
    private String billingScheduleId;
    private Boolean isSuccess;
    private String errorCode;
    private String errorMessage;

    public String getOrderItemId() {
        return this.orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }
    public String getBillingScheduleId() {
        return this.billingScheduleId;
    }

    public void setBillingScheduleId(String billingScheduleId) {
        this.billingScheduleId = billingScheduleId;
    }
    public Boolean getIsSuccess() {
        return this.isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "{BillSchdCreatedEventDetail{" +
        ",orderItemId='" + orderItemId + "'" +
        ",billingScheduleId='" + billingScheduleId + "'" +
        ",isSuccess='" + isSuccess + "'" +
        ",errorCode='" + errorCode + "'" +
        ",errorMessage='" + errorMessage + "'" +
        '}';
    }
}