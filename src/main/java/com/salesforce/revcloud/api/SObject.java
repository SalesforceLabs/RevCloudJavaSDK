package com.salesforce.revcloud.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salesforce.revcloud.api.serializer.SObjectDeserializer;
import com.salesforce.revcloud.api.serializer.SObjectSerializer;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(using = SObjectSerializer.class)
@JsonDeserialize(using = SObjectDeserializer.class)
public class SObject {

    private String id;
    private Map<String, String> attributes = new HashMap<>();
    private Map<String, Object> fields = new HashMap<>();

    public SObject(String type) {
        attributes.put("type", type);
    }

    public String getId() {
        return getField("Id");
    }

    public void setId(String id) {
        setField("Id", id);
    }

    public void setField(String key, Object value) {
        this.fields.put(key, value);
    }

    public <T> T getField(String key) {
        return (T)this.fields.get(key);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }
}
