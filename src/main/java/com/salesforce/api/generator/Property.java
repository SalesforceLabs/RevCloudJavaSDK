package com.salesforce.api.generator;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

public class Property {

    private String name;
    private String type;
    private boolean required = false;

    public Property(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getCapitalizedName() {
        return StringUtils.capitalize(this.name);
    }

    public String getDataType() {
        if (this.type == null) {
            if ("map".equals(name)) {
                return "Map<String, String>";
            } else {
                throw new RuntimeException("Missing data type for " + this);
            }
        }
        if (this.type.indexOf("|") > -1) {
            return "Object";
        }
        if (this.type.indexOf(".raml") > -1) {
            RamlToApiGenerator.addAdditionalTypes(this.type);
            return this.type.split("\\.")[0];
        }
        switch (this.type.toLowerCase()) {
            case "id":
            case "string":
                return "String";
            case "integer":
            case "boolean":
                return StringUtils.capitalize(this.type);
            case "number":
                return "Double";
            case "date-only":
                return "Date";
            case "array":
                return "List";
            default:
                return this.type;
        }
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", required=" + required +
                '}';
    }
}
