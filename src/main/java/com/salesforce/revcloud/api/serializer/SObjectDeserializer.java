package com.salesforce.revcloud.api.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.salesforce.revcloud.api.SObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SObjectDeserializer extends JsonDeserializer<SObject> {

    @Override
    public SObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);
        SObject result = new SObject(node.path("attributes").path("type").asText());
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        while(iterator.hasNext()) {
            Map.Entry<String, JsonNode> next = iterator.next();
            if (!"attributes".equals(next.getKey())) {
                Object value = next.getValue();
                if (value instanceof BooleanNode) {
                    result.setField(next.getKey(), next.getValue().asBoolean());
                } else if (value instanceof IntNode) {
                    result.setField(next.getKey(), next.getValue().asInt());
                } else if (value instanceof DoubleNode) {
                    result.setField(next.getKey(), next.getValue().asDouble());
                } else if (value instanceof LongNode) {
                    result.setField(next.getKey(), next.getValue().asLong());
                } else if (value instanceof TextNode) {
                    result.setField(next.getKey(), next.getValue().asText());
                } else if (value instanceof ArrayNode) {
                    ArrayNode arrayNode = (ArrayNode)value;
                    JsonNode attrNode = arrayNode.findValue("attributes");
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.findAndRegisterModules();
                    boolean isNestedSObject = attrNode != null && attrNode.get("type") != null;
                    List children = new ArrayList<>(arrayNode.size());
                    for (int i = 0; i < arrayNode.size(); i ++) {
                        if (isNestedSObject) {
                            children.add(mapper.readValue(arrayNode.get(0).toString(), SObject.class));
                        } else {
                            children.add(mapper.readValue(arrayNode.get(0).toString(), String.class));
                        }
                    }
                    result.setField(next.getKey(), children);
                } else {
                    throw new IllegalArgumentException("Unsupported node encountered during deserialization: " + value);
                }
            }
        }
        return result;
    }
}
