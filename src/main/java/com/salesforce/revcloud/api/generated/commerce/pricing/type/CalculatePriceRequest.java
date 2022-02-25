package com.salesforce.revcloud.api.generated.commerce.pricing.type;

import java.util.Date;
import java.util.List;

/**
 * The payload of a calculate price request.
 */
public class CalculatePriceRequest {
    private String listPricebookId;
    private List candidatePricebookIds;
    private String pricingFlow;
    private String roundingMode;
    private String startDate;
    private String endDate;
    private Integer subscriptionTerm;
    private String subscriptionTermUnit;
    private Graph graph;

    public String getListPricebookId() {
        return this.listPricebookId;
    }

    public CalculatePriceRequest setListPricebookId(String value) {
        this.listPricebookId = value;
        return this;
    }

    public List getCandidatePricebookIds() {
        return this.candidatePricebookIds;
    }

    public CalculatePriceRequest setCandidatePricebookIds(List value) {
        this.candidatePricebookIds = value;
        return this;
    }

    public String getPricingFlow() {
        return this.pricingFlow;
    }

    public CalculatePriceRequest setPricingFlow(String value) {
        this.pricingFlow = value;
        return this;
    }

    public String getRoundingMode() {
        return this.roundingMode;
    }

    public CalculatePriceRequest setRoundingMode(String value) {
        this.roundingMode = value;
        return this;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public CalculatePriceRequest setStartDate(String value) {
        this.startDate = value;
        return this;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public CalculatePriceRequest setEndDate(String value) {
        this.endDate = value;
        return this;
    }

    public Integer getSubscriptionTerm() {
        return this.subscriptionTerm;
    }

    public CalculatePriceRequest setSubscriptionTerm(Integer value) {
        this.subscriptionTerm = value;
        return this;
    }

    public String getSubscriptionTermUnit() {
        return this.subscriptionTermUnit;
    }

    public CalculatePriceRequest setSubscriptionTermUnit(String value) {
        this.subscriptionTermUnit = value;
        return this;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public CalculatePriceRequest setGraph(Graph value) {
        this.graph = value;
        return this;
    }

}