package com.salesforce.revcloud.api.generated.commerce.pricing.type;

import java.util.Date;
import java.util.List;

/**
 * Interface that defines a discount for a sales transaction item.
 */
public class PriceAdjustmentItem {
    private Attributes attributes;
    private String currencyIsoCode;
    private String salesTransactionItemId;
    private String priceAdjustmentGroupId;
    private String adjustmentSource;
    private String description;
    private Double totalAmount;
    private String adjustmentType;
    private Double adjustmentValue;
    private Double priority;
    private String priceAdjustmentCauseId;
    private String adjustmentAmountScope;

    public Attributes getAttributes() {
        return this.attributes;
    }

    public PriceAdjustmentItem setAttributes(Attributes value) {
        this.attributes = value;
        return this;
    }

    public String getCurrencyIsoCode() {
        return this.currencyIsoCode;
    }

    public PriceAdjustmentItem setCurrencyIsoCode(String value) {
        this.currencyIsoCode = value;
        return this;
    }

    public String getSalesTransactionItemId() {
        return this.salesTransactionItemId;
    }

    public PriceAdjustmentItem setSalesTransactionItemId(String value) {
        this.salesTransactionItemId = value;
        return this;
    }

    public String getPriceAdjustmentGroupId() {
        return this.priceAdjustmentGroupId;
    }

    public PriceAdjustmentItem setPriceAdjustmentGroupId(String value) {
        this.priceAdjustmentGroupId = value;
        return this;
    }

    public String getAdjustmentSource() {
        return this.adjustmentSource;
    }

    public PriceAdjustmentItem setAdjustmentSource(String value) {
        this.adjustmentSource = value;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public PriceAdjustmentItem setDescription(String value) {
        this.description = value;
        return this;
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public PriceAdjustmentItem setTotalAmount(Double value) {
        this.totalAmount = value;
        return this;
    }

    public String getAdjustmentType() {
        return this.adjustmentType;
    }

    public PriceAdjustmentItem setAdjustmentType(String value) {
        this.adjustmentType = value;
        return this;
    }

    public Double getAdjustmentValue() {
        return this.adjustmentValue;
    }

    public PriceAdjustmentItem setAdjustmentValue(Double value) {
        this.adjustmentValue = value;
        return this;
    }

    public Double getPriority() {
        return this.priority;
    }

    public PriceAdjustmentItem setPriority(Double value) {
        this.priority = value;
        return this;
    }

    public String getPriceAdjustmentCauseId() {
        return this.priceAdjustmentCauseId;
    }

    public PriceAdjustmentItem setPriceAdjustmentCauseId(String value) {
        this.priceAdjustmentCauseId = value;
        return this;
    }

    public String getAdjustmentAmountScope() {
        return this.adjustmentAmountScope;
    }

    public PriceAdjustmentItem setAdjustmentAmountScope(String value) {
        this.adjustmentAmountScope = value;
        return this;
    }

}