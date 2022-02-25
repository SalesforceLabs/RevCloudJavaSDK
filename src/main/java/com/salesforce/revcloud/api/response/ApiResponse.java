package com.salesforce.revcloud.api.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiResponse {

    private String actionName;
    private List<Object> errors = new ArrayList<>();
    private String requestId;
    private String correlationId;
    private boolean success;
    private boolean asyncEventReceived;
    private Map<String, Object> apiOutput;

    public ApiResponse() {}

    public ApiResponse(String actionName, boolean success, List<Object> errors, String requestId) {
        this.actionName = actionName;
        this.success = success;
        this.errors = errors;
        this.requestId = requestId;
    }

    public String getActionName() {
        return actionName;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void addError(Object error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }

    public String getRequestId() {
        return requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isAsyncEventReceived() {
        return asyncEventReceived;
    }

    public void setAsyncEventReceived(boolean asyncEventReceived) {
        this.asyncEventReceived = asyncEventReceived;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Map<String, Object> getApiOutput() {
        return apiOutput;
    }

    public void setApiOutput(Map<String, Object> apiOutput) {
        this.apiOutput = apiOutput;
    }

    @Override
    public String toString() {
        StringBuilder errorContent = new StringBuilder();
        if (errors != null) {
            for (Object error : errors) {
                if (errorContent.length() > 0) {
                    errorContent.append(", ");
                }
                errorContent.append(error);
            }
        }
        return "ApiResponse{" +
                "actionName='" + actionName + '\'' +
                ", success=" + success +
                ", requestId='" + requestId + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", asyncEventReceived=" + asyncEventReceived +
                ", errors=[" + errorContent +
                "]}";
    }
}
