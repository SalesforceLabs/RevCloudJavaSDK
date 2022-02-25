package com.salesforce.api.generator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventField extends Event {

    private String dataType;

    private boolean isArray;

    public String getDataType() {
        return dataType;
    }

    private boolean isComplexValueType;

    private static final Map<String, EventField> COMPLEX_FIELD_CACHE = new HashMap<>();

    @JsonProperty("doc")
    public void setDataType(String value) {
        String rawType = value.split(":")[1];
        switch (rawType.toLowerCase()) {
            case "datetime":
            case "entityid":
            case "text":
            case "string":
            case "stringplusclob":
            case "staticenum":
            case "multilinetext":
                this.dataType = "String";
                break;
            case "complexvaluetype":
                this.isComplexValueType = true;
                break;
            default:
                this.dataType = rawType;
                break;
        }
    }

    @JsonProperty("type")
    public void processComplexValueType(Object value) {
        if (value instanceof List && ((List)value).size() == 2) {
            Object firstElem = ((List)value).get(1);
            if (firstElem instanceof Map) {
                Map<String, Object> typeMap = (Map<String, Object>) ((List) value).get(1);
                this.isArray = "array".equals(typeMap.get("type"));
                Map<String, Object> itemsMap = (Map<String, Object>) typeMap.get("items");
                this.dataType = (String) itemsMap.get("name");
                this.className = dataType;
                List<Map<String, Object>> fieldsList = (List<Map<String, Object>>) itemsMap.get("fields");
                this.fields = new ArrayList<>(fieldsList.size());
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                for (Map<String, Object> fieldMap : fieldsList) {
                    EventField field = mapper.convertValue(fieldMap, EventField.class);
                    field.setParentEvent(this);
                    this.fields.add(field);
                }
                COMPLEX_FIELD_CACHE.put(getClassName(), this);
            } else if (firstElem instanceof String && ((String)firstElem).indexOf(".") > -1) {
                // data type contains a fully qualified java class but it doesn't have complete description
                String firstElemStr = (String)firstElem;
                String[] parts = firstElemStr.split("\\.");
                this.dataType = parts[parts.length - 1];
                if (COMPLEX_FIELD_CACHE.containsKey(this.getClassName())) {
                    this.fields = new ArrayList<>();
                    this.fields.addAll(COMPLEX_FIELD_CACHE.get(this.getClassName()).getFields());
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public boolean isArray() {
        return isArray;
    }

    public String getCapitalizedName() {
        return StringUtils.capitalize(name);
    }

    public boolean isComplex() {
        return this.isComplexValueType || (this.fields != null && this.fields.size() > 0);
    }

    public String getPluralName() {
        return name.endsWith("s") ? name : name + "s";
    }

    public String getCapitalizedPluralName() {
        return StringUtils.capitalize(getPluralName());
    }

    public boolean isDateType() {
        return "Date".equals(this.dataType);
    }

    public boolean isSObjectType() {
        return "SObject".equals(this.dataType);
    }
}
