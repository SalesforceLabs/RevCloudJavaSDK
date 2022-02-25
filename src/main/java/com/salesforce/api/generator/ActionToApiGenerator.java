package com.salesforce.api.generator;

import com.google.common.collect.ImmutableMap;
import com.salesforce.revcloud.ApiContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API Generator for Invocable Actions
 */
public class ActionToApiGenerator extends BaseApiGenerator {

    public ActionToApiGenerator(ApiContext apiContext, String packageName, String destination) {
        super(apiContext, packageName, destination);
    }

    public void generateInternal(ManifestEntry manifestEntry, VelocityEngine ve, String packageName, Event eventModel) throws Exception {
        VelocityContext context = new VelocityContext();
        Template template;
        FileWriter writer;
        // Generate ApiResponse class
        template = ve.getTemplate("Response.wm");

        Map<String, Object> apiDescription = (Map<String, Object>)describe(manifestEntry.getEndpoint(), Map.class);
        String apiName = StringUtils.capitalize((String) apiDescription.get("name"));
        context.put("api", apiName);
        List<Map<String, String>> outputFields = buildFields(apiName, (List) apiDescription.get("outputs"));
        if (eventModel != null) {
            outputFields.add(ImmutableMap.of(
                    "name", eventModel.getCamelCaseName(),
                    "capitalized", eventModel.getCapitalizedName(),
                    "type", eventModel.getClassName()));
            context.put("hasEvent", true);
        }
        context.put("fields", outputFields);
        context.put("package", packageName);
        writer = new FileWriter(getDestinationFile("response", apiName + "Response"));
        try {
            template.merge(context, writer);
        } finally {
            writer.close();
        }

        // generate API class
        template = ve.getTemplate("Api.wm");
        context = new VelocityContext();

        context.put("api", apiName);
        context.put("baseApi", "AbstractApi");
        context.put("inputFields", buildFields(apiName, (List) apiDescription.get("inputs")));
        context.put("outputFields", buildFields(apiName, (List) apiDescription.get("outputs")));
        context.put("populateEventCode", eventModel != null ? eventModel.getInnerPopulateCode() : "");
        context.put("endpoint", manifestEntry.getEndpoint());
        context.put("package", packageName);
        context.put("event", eventModel);
        context.put("method", "HttpMethod." + (manifestEntry.getMethod() == null ? "POST" : manifestEntry.getMethod()));
        context.put("apiResponse", apiName + "Response");

        writer = new FileWriter(getDestinationFile("", apiName));
        try {
            template.merge(context, writer);
        } finally {
            writer.close();
        }
    }

    private List<Map<String, String>> buildFields(String apiName, List inputFields) {
        List<Map<String, String>> result = new ArrayList<>();
        for (Object fieldObj : inputFields) {
            Map<String, String> fieldMap = new HashMap<>();
            Map<String, Object> fieldData = (Map<String, Object>)fieldObj;
            int maxOccurs = (Integer)fieldData.get("maxOccurs");
            fieldMap.put("type", getDataType((String)fieldData.get("type"), maxOccurs));
            fieldMap.put("capitalized", StringUtils.capitalize((String)fieldData.get("name")));
            fieldMap.put("name", (String)fieldData.get("name"));
            result.add(fieldMap);
        }
        return result;
    }

    private String getDataType(String apiDataType, int maxOccur) {
        String type;
        switch (apiDataType) {
            case "ID":
            case "STRING":
            case "PICKLIST":
                type = "String";
                break;
            case "SOBJECT":
                type = "SObject";
                break;
            case "DATETIME":
                type = "Date";
                break;
            default:
                type = StringUtils.capitalize(apiDataType.toLowerCase());
                break;
        }
        if (maxOccur > 1) {
            return "List<" + type + ">";
        } else {
            return type;
        }
    }
}
