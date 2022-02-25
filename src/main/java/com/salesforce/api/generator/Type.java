package com.salesforce.api.generator;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a RAML data type
 */
public class Type {

    private String name;
    private String description;
    private String type;
    private List<Property> properties = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Property> getProperties() {
        return properties;
    }

    @JsonProperty("displayName")
    public void setDisplayName(String displayNameValue) {
        if (this.name == null && displayNameValue != null) {
            this.name = displayNameValue;
        }
    }

    @JsonProperty("properties")
    public void setProperties(Object propertiesValue) {
        Map<String, Object> propertiesMap = (Map<String, Object>)propertiesValue;
        for (Map.Entry<String, Object> entry : propertiesMap.entrySet()) {
            String propName = entry.getKey();
            Property prop = new Property(propName.substring(0, 1).toLowerCase() + propName.substring(1));
            Map<String, Object> value = (Map<String, Object>)entry.getValue();
            prop.setRequired(Boolean.TRUE.equals(value.get("required")));
            prop.setType((String)value.get("type"));
            properties.add(prop);
        }
    }

    @Override
    public String toString() {
        return "Type{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", properties=" + properties +
                '}';
    }
}
