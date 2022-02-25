package com.salesforce.revcloud.api.generated.commerce.pricing.type;

import java.util.Date;
import java.util.List;

/**
 * SObject Graph Record with Reference Id
 */
public class Record {
    private String referenceId;
    private Object record;

    public String getReferenceId() {
        return this.referenceId;
    }

    public Record setReferenceId(String value) {
        this.referenceId = value;
        return this;
    }

    public Object getRecord() {
        return this.record;
    }

    public Record setRecord(Object value) {
        this.record = value;
        return this;
    }

}