/**
 * QuoteToOrderErrDtlEvent.java
 *
 * Event class for QuoteToOrderErrDtlEvent
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
public class QuoteToOrderErrDtlEvent {

    private String revenueTransactionErrLogId;
    private String primaryRecordId;
    private String relatedRecordId;
    private String errorCode;
    private String errorMessage;

    public String getRevenueTransactionErrLogId() {
        return this.revenueTransactionErrLogId;
    }

    public void setRevenueTransactionErrLogId(String revenueTransactionErrLogId) {
        this.revenueTransactionErrLogId = revenueTransactionErrLogId;
    }
    public String getPrimaryRecordId() {
        return this.primaryRecordId;
    }

    public void setPrimaryRecordId(String primaryRecordId) {
        this.primaryRecordId = primaryRecordId;
    }
    public String getRelatedRecordId() {
        return this.relatedRecordId;
    }

    public void setRelatedRecordId(String relatedRecordId) {
        this.relatedRecordId = relatedRecordId;
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
        return "{QuoteToOrderErrDtlEvent{" +
        ",revenueTransactionErrLogId='" + revenueTransactionErrLogId + "'" +
        ",primaryRecordId='" + primaryRecordId + "'" +
        ",relatedRecordId='" + relatedRecordId + "'" +
        ",errorCode='" + errorCode + "'" +
        ",errorMessage='" + errorMessage + "'" +
        '}';
    }
}
