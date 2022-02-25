package com.salesforce.revcloud.api.generated.commerce.pricing.type;

import java.util.Date;
import java.util.List;

/**
 * Defines the properties of a sales transaction.
 */
public class SalesTransaction {
    private Attributes attributes;
    private String currencyIsoCode;
    private Double totalListAmount;
    private Double totalAmount;
    private Double totalProductAmount;
    private Double totalAdjustmentAmount;
    private Double totalAdjustmentDistAmount;

    public Attributes getAttributes() {
        return this.attributes;
    }

    public SalesTransaction setAttributes(Attributes value) {
        this.attributes = value;
        return this;
    }

    public String getCurrencyIsoCode() {
        return this.currencyIsoCode;
    }

    public SalesTransaction setCurrencyIsoCode(String value) {
        this.currencyIsoCode = value;
        return this;
    }

    public Double getTotalListAmount() {
        return this.totalListAmount;
    }

    public SalesTransaction setTotalListAmount(Double value) {
        this.totalListAmount = value;
        return this;
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public SalesTransaction setTotalAmount(Double value) {
        this.totalAmount = value;
        return this;
    }

    public Double getTotalProductAmount() {
        return this.totalProductAmount;
    }

    public SalesTransaction setTotalProductAmount(Double value) {
        this.totalProductAmount = value;
        return this;
    }

    public Double getTotalAdjustmentAmount() {
        return this.totalAdjustmentAmount;
    }

    public SalesTransaction setTotalAdjustmentAmount(Double value) {
        this.totalAdjustmentAmount = value;
        return this;
    }

    public Double getTotalAdjustmentDistAmount() {
        return this.totalAdjustmentDistAmount;
    }

    public SalesTransaction setTotalAdjustmentDistAmount(Double value) {
        this.totalAdjustmentDistAmount = value;
        return this;
    }

}