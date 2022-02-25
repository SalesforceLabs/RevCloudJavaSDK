package com.salesforce.revcloud.api.response;

import java.util.List;
import java.util.Map;

public class SoqlQueryResponse extends ApiResponse {

    private int totalSize;
    private boolean done;
    private List<Map<String, Object>> records;

    public SoqlQueryResponse(String actionName, boolean success, List<Object> errors, String requestId) {
        super(actionName, success, errors, requestId);
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public List<Map<String, Object>> getRecords() {
        return records;
    }

    public void setRecords(List<Map<String, Object>> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return super.toString() + "{SoqlQueryResponse{" +
                "totalSize=" + totalSize +
                ", done=" + done +
                ", records=[" + records + "]" +
                '}';
    }
}
