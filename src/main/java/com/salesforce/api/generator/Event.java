package com.salesforce.api.generator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Event {

    @JsonIgnore
    List<EventField> fields;
    String name;
    String className;
    private Event parentEvent;

    public List<EventField> getFields() {
        return fields;
    }

    @JsonProperty("fields")
    public void parseFields(List<Map<String, Object>> values) {
        Map<String, Object> typeMap = (Map<String, Object>)values.get(0).get("type");
        typeMap = (Map<String, Object>)((List<Map<String, Object>>)typeMap.get("fields")).get(1).get("type");
        List<Map<String, Object>> fieldsList = (List<Map<String, Object>>)typeMap.get("fields");
        this.fields = new ArrayList<>(fieldsList.size());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        for (Map<String, Object> fieldMap : fieldsList) {
            EventField field = mapper.convertValue(fieldMap, EventField.class);
            field.setParentEvent(this);
            this.fields.add(field);
        }
    }

    public String getClassName() {
        if (this.className != null) {
            return StringUtils.capitalize(this.className);
        } else {
            return getCapitalizedName();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapitalizedName() {
        return StringUtils.capitalize(name);
    }

    public String getCamelCaseName() {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public String getCamelCaseClassName() {
        String className = this.className == null ? this.name : this.className;
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    public Event getParentEvent() {
        return parentEvent;
    }

    public void setParentEvent(Event parentEvent) {
        this.parentEvent = parentEvent;
    }

    public String getInnerPopulateCode() {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER_PATHS, this.getClass().getClassLoader().getResource(".").getFile());
        ve.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        ve.init();
        Template template = ve.getTemplate("PopulateEventCode.wm");
        Context context = new VelocityContext();
        context.put("event", this);
        context.put("payloadVar", getParentEvent() != null ? getName() + "Map" : "payload");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
}
