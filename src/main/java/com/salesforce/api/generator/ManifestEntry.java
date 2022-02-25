package com.salesforce.api.generator;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for entries in manifest.yaml
 */
public class ManifestEntry {

    private String endpoint;
    private String event;
    private String raml;
    private String className;
    private String packageName;
    private String method;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getRaml() {
        return raml;
    }

    public void setRaml(String raml) {
        this.raml = raml;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    @JsonProperty("package")
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "ManifestEntry{" +
                "endpoint='" + endpoint + '\'' +
                ", event='" + event + '\'' +
                ", raml='" + raml + '\'' +
                ", className='" + className + '\'' +
                ", packageName='" + packageName + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
