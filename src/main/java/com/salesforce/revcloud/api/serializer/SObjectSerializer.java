package com.salesforce.revcloud.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.salesforce.revcloud.api.SObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class SObjectSerializer extends JsonSerializer<SObject> {

    @Override
    public void serialize(SObject sobject, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("attributes", sobject.getAttributes());
        if (sobject.getFields() != null) {
            for (Map.Entry<String, Object> entry : sobject.getFields().entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Date && value != null) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    value = format.format(value);
                }
                jsonGenerator.writeObjectField(entry.getKey(), value);
            }
        }
        jsonGenerator.writeEndObject();
    }
}
