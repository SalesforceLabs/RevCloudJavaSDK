package com.salesforce.revcloud.api.generated.commerce.pricing.type;

import java.util.Date;
import java.util.List;

/**
 * Defines the top-level discount for a sales transaction. An example of a top-level discount is 10% off an entire order.
 */
public class PriceAdjustmentGroup {
    private Attributes attributes;
    private String currencyIsoCode;
    private String salesTransactionId;
    private String description;
    private Double totalAmount;
    private String adjustmentType;
    private Double adjustmentValue;
    private String adjustmentSource;
    private Double priority;
    private String priceAdjustmentCauseId;

    public Attributes getAttributes() {
        return this.attributes;
    }

    public PriceAdjustmentGroup setAttributes(Attributes value) {
        this.attributes = value;
        return this;
    }

    public String getCurrencyIsoCode() {
        return this.currencyIsoCode;
    }

    public PriceAdjustmentGroup setCurrencyIsoCode(String value) {
        this.currencyIsoCode = value;
        return this;
    }

    public String getSalesTransactionId() {
        return this.salesTransactionId;
    }

    public PriceAdjustmentGroup setSalesTransactionId(String value) {
        this.salesTransactionId = value;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public PriceAdjustmentGroup setDescription(String value) {
        this.description = value;
        return this;
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public PriceAdjustmentGroup setTotalAmount(Double value) {
        this.totalAmount = value;
        return this;
    }

    public String getAdjustmentType() {
        return this.adjustmentType;
    }

    public PriceAdjustmentGroup setAdjustmentType(String value) {
        this.adjustmentType = value;
        return this;
    }

    public Double getAdjustmentValue() {
        return this.adjustmentValue;
    }

    public PriceAdjustmentGroup setAdjustmentValue(Double value) {
        this.adjustmentValue = value;
        return this;
    }

    public String getAdjustmentSource() {
        return this.adjustmentSource;
    }

    public PriceAdjustmentGroup setAdjustmentSource(String value) {
        this.adjustmentSource = value;
        return this;
    }

    public Double getPriority() {
        return this.priority;
    }

    public PriceAdjustmentGroup setPriority(Double value) {
        this.priority = value;
        return this;
    }

    public String getPriceAdjustmentCauseId() {
        return this.priceAdjustmentCauseId;
    }

    public PriceAdjustmentGroup setPriceAdjustmentCauseId(String value) {
        this.priceAdjustmentCauseId = value;
        return this;
    }

}