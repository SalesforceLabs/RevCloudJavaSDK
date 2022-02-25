package com.salesforce.revcloud.api.generated.commerce.pricing.type;

import java.util.Date;
import java.util.List;

/**
 * A collection of SObjects to be priced as part of a transaction.
 */
public class Graph {
    private String graphId;
    private Record[] records;

    public String getGraphId() {
        return this.graphId;
    }

    public Graph setGraphId(String value) {
        this.graphId = value;
        return this;
    }

    public Record[] getRecords() {
        return this.records;
    }

    public Graph setRecords(Record[] value) {
        this.records = value;
        return this;
    }

}