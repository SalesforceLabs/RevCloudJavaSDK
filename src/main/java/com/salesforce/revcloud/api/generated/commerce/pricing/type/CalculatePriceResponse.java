package com.salesforce.revcloud.api.generated.commerce.pricing.type;

import java.util.Date;
import java.util.List;

/**
 * The result of the calculate-price request.
 */
public class CalculatePriceResponse {
    private String graphId;
    private List records;

    public String getGraphId() {
        return this.graphId;
    }

    public CalculatePriceResponse setGraphId(String value) {
        this.graphId = value;
        return this;
    }

    public List getRecords() {
        return this.records;
    }

    public CalculatePriceResponse setRecords(List value) {
        this.records = value;
        return this;
    }

}