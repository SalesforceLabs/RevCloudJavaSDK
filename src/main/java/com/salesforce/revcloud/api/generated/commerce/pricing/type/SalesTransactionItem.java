package com.salesforce.revcloud.api.generated.commerce.pricing.type;

import java.util.Date;
import java.util.List;

/**
 * Defines the properties of a line-level object in a sales transaction; for example, an order item in an order.
 */
public class SalesTransactionItem {
    private Attributes attributes;
    private String salesTransactionId;
    private String productId;
    private String currencyIsoCode;
    private String salesItemType;
    private Double quantity;
    private Double listPrice;
    private Double startingUnitPrice;
    private String startingUnitPriceSource;
    private Double netUnitPrice;
    private Double listPriceTotal;
    private Double startingPriceTotal;
    private Double totalLineAmount;
    private Double totalAdjustmentAmount;
    private Double totalAdjustmentDistAmount;
    private Double totalPrice;
    private String stockKeepingUnit;
    private String pricebookEntryId;
    private String productSellingModelId;
    private Date startDate;
    private Date endDate;
    private Double subscriptionTerm;
    private Double proFormaBillingPeriodAmount;
    private Double pricingTermCount;
    private String billingFrequency;
    private String periodBoundary;
    private Double periodBoundaryDay;
    private String prorationPolicyId;
    private String pricingTransactionType;
    private String basisTransactionItemId;

    public Attributes getAttributes() {
        return this.attributes;
    }

    public SalesTransactionItem setAttributes(Attributes value) {
        this.attributes = value;
        return this;
    }

    public String getSalesTransactionId() {
        return this.salesTransactionId;
    }

    public SalesTransactionItem setSalesTransactionId(String value) {
        this.salesTransactionId = value;
        return this;
    }

    public String getProductId() {
        return this.productId;
    }

    public SalesTransactionItem setProductId(String value) {
        this.productId = value;
        return this;
    }

    public String getCurrencyIsoCode() {
        return this.currencyIsoCode;
    }

    public SalesTransactionItem setCurrencyIsoCode(String value) {
        this.currencyIsoCode = value;
        return this;
    }

    public String getSalesItemType() {
        return this.salesItemType;
    }

    public SalesTransactionItem setSalesItemType(String value) {
        this.salesItemType = value;
        return this;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public SalesTransactionItem setQuantity(Double value) {
        this.quantity = value;
        return this;
    }

    public Double getListPrice() {
        return this.listPrice;
    }

    public SalesTransactionItem setListPrice(Double value) {
        this.listPrice = value;
        return this;
    }

    public Double getStartingUnitPrice() {
        return this.startingUnitPrice;
    }

    public SalesTransactionItem setStartingUnitPrice(Double value) {
        this.startingUnitPrice = value;
        return this;
    }

    public String getStartingUnitPriceSource() {
        return this.startingUnitPriceSource;
    }

    public SalesTransactionItem setStartingUnitPriceSource(String value) {
        this.startingUnitPriceSource = value;
        return this;
    }

    public Double getNetUnitPrice() {
        return this.netUnitPrice;
    }

    public SalesTransactionItem setNetUnitPrice(Double value) {
        this.netUnitPrice = value;
        return this;
    }

    public Double getListPriceTotal() {
        return this.listPriceTotal;
    }

    public SalesTransactionItem setListPriceTotal(Double value) {
        this.listPriceTotal = value;
        return this;
    }

    public Double getStartingPriceTotal() {
        return this.startingPriceTotal;
    }

    public SalesTransactionItem setStartingPriceTotal(Double value) {
        this.startingPriceTotal = value;
        return this;
    }

    public Double getTotalLineAmount() {
        return this.totalLineAmount;
    }

    public SalesTransactionItem setTotalLineAmount(Double value) {
        this.totalLineAmount = value;
        return this;
    }

    public Double getTotalAdjustmentAmount() {
        return this.totalAdjustmentAmount;
    }

    public SalesTransactionItem setTotalAdjustmentAmount(Double value) {
        this.totalAdjustmentAmount = value;
        return this;
    }

    public Double getTotalAdjustmentDistAmount() {
        return this.totalAdjustmentDistAmount;
    }

    public SalesTransactionItem setTotalAdjustmentDistAmount(Double value) {
        this.totalAdjustmentDistAmount = value;
        return this;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public SalesTransactionItem setTotalPrice(Double value) {
        this.totalPrice = value;
        return this;
    }

    public String getStockKeepingUnit() {
        return this.stockKeepingUnit;
    }

    public SalesTransactionItem setStockKeepingUnit(String value) {
        this.stockKeepingUnit = value;
        return this;
    }

    public String getPricebookEntryId() {
        return this.pricebookEntryId;
    }

    public SalesTransactionItem setPricebookEntryId(String value) {
        this.pricebookEntryId = value;
        return this;
    }

    public String getProductSellingModelId() {
        return this.productSellingModelId;
    }

    public SalesTransactionItem setProductSellingModelId(String value) {
        this.productSellingModelId = value;
        return this;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public SalesTransactionItem setStartDate(Date value) {
        this.startDate = value;
        return this;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public SalesTransactionItem setEndDate(Date value) {
        this.endDate = value;
        return this;
    }

    public Double getSubscriptionTerm() {
        return this.subscriptionTerm;
    }

    public SalesTransactionItem setSubscriptionTerm(Double value) {
        this.subscriptionTerm = value;
        return this;
    }

    public Double getProFormaBillingPeriodAmount() {
        return this.proFormaBillingPeriodAmount;
    }

    public SalesTransactionItem setProFormaBillingPeriodAmount(Double value) {
        this.proFormaBillingPeriodAmount = value;
        return this;
    }

    public Double getPricingTermCount() {
        return this.pricingTermCount;
    }

    public SalesTransactionItem setPricingTermCount(Double value) {
        this.pricingTermCount = value;
        return this;
    }

    public String getBillingFrequency() {
        return this.billingFrequency;
    }

    public SalesTransactionItem setBillingFrequency(String value) {
        this.billingFrequency = value;
        return this;
    }

    public String getPeriodBoundary() {
        return this.periodBoundary;
    }

    public SalesTransactionItem setPeriodBoundary(String value) {
        this.periodBoundary = value;
        return this;
    }

    public Double getPeriodBoundaryDay() {
        return this.periodBoundaryDay;
    }

    public SalesTransactionItem setPeriodBoundaryDay(Double value) {
        this.periodBoundaryDay = value;
        return this;
    }

    public String getProrationPolicyId() {
        return this.prorationPolicyId;
    }

    public SalesTransactionItem setProrationPolicyId(String value) {
        this.prorationPolicyId = value;
        return this;
    }

    public String getPricingTransactionType() {
        return this.pricingTransactionType;
    }

    public SalesTransactionItem setPricingTransactionType(String value) {
        this.pricingTransactionType = value;
        return this;
    }

    public String getBasisTransactionItemId() {
        return this.basisTransactionItemId;
    }

    public SalesTransactionItem setBasisTransactionItemId(String value) {
        this.basisTransactionItemId = value;
        return this;
    }

}