package com.salesforce.api.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.salesforce.revcloud.ApiContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * API Generator for RAML files
 */
public class RamlToApiGenerator extends BaseApiGenerator {

    private static ThreadLocal<String> currentRamlPath = new ThreadLocal<>();
    private static Set<String> ADDITIONAL_TYPES = new HashSet<>();

    public RamlToApiGenerator(ApiContext apiContext, String packageName, String destination) {
        super(apiContext, packageName, destination);
    }

    public static void addAdditionalTypes(String typeRamlPath) {
        String path = currentRamlPath.get() == null ? typeRamlPath : currentRamlPath.get() + "/../" + typeRamlPath;
        RamlToApiGenerator.ADDITIONAL_TYPES.add(path);
    }

    public void generateInternal(ManifestEntry manifestEntry, VelocityEngine ve, String packageName, Event eventModel) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode result = mapper.readValue(this.getClass().getClassLoader().getResource(manifestEntry.getRaml()).openConnection().getURL(), JsonNode.class);
        String inputType = result.path(manifestEntry.getEndpoint()).path("post").path("body").path("application/json").path("type").asText();
        System.out.println("Got input type: " + inputType);
        Map<String, Object> types = mapper.convertValue(result.path("types"), new TypeReference<Map<String, Object>>(){});
        generateTypes(manifestEntry, ve, mapper, types, packageName);
        generateApi(ve, manifestEntry.getClassName(), manifestEntry.getEndpoint(), packageName);
        generateResponse(ve, manifestEntry.getClassName(), packageName);
    }

    private void generateApi(VelocityEngine ve, String api, String endpoint, String packageName) throws IOException {
        Template template;
        template = ve.getTemplate("Api.wm");
        VelocityContext context = new VelocityContext();
        context.put("api", api);
        context.put("baseApi", "AbstractConnectApi");
        context.put("package", packageName);
        context.put("endpoint", endpoint);
        context.put("method", "HttpMethod.POST");
        context.put("apiResponse", api + "Response");
        context.put("hasTypes", true);
        FileWriter writer = new FileWriter(getDestinationFile("", api, packageName));
        try {
            template.merge(context, writer);
        } finally {
            writer.close();
        }
    }

    private void generateTypes(ManifestEntry manifestEntry, VelocityEngine ve, ObjectMapper mapper, Map<String, Object> types, String packageName) throws Exception {
        for (Map.Entry<String, Object> entry : types.entrySet()) {
            // ID type is treated as String
            if (!"ID".equals(entry.getKey())) {
                Type type;
                Map<String, Object> values = (Map<String, Object>)entry.getValue();
                if (values.get("type") != null && values.get("type").toString().endsWith(".raml")) {
                    String path = manifestEntry.getRaml() + "/../" + values.get("type");
                    URL url = this.getClass().getClassLoader().getResource(path);
                    if (url == null) {
                        throw new RuntimeException("Cannot locate RAML file " + path);
                    } else {
                        currentRamlPath.set(path);
                        type = mapper.readValue(url, Type.class);
                    }
                } else {
                    type = mapper.convertValue(entry.getValue(), Type.class);
                }
                generateType(ve, mapper, entry.getKey(), type, packageName);
            }
        }
        while (ADDITIONAL_TYPES.size() > 0) {
            generateAdditionalTypes(ve, packageName);
        }
    }

    private void generateAdditionalTypes(VelocityEngine ve, String packageName) throws Exception {
        Set<String> addlTypes = new HashSet<>();
        addlTypes.addAll(ADDITIONAL_TYPES);
        ADDITIONAL_TYPES.clear();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        for (String path : addlTypes) {
            URL url = this.getClass().getClassLoader().getResource(path);
            if (url == null) {
                throw new RuntimeException("Cannot locate RAML file " + path);
            } else {
                Type type = mapper.readValue(url, Type.class);
                generateType(ve, mapper, StringUtils.capitalize(type.getName()), type, packageName);
            }
        }
    }

    private void generateType(VelocityEngine ve, ObjectMapper mapper, String name, Type type, String packageName) throws Exception {
        type.setName(name);
        VelocityContext context = new VelocityContext();
        context.put("package", packageName + ".type");
        context.put("type", type);
        FileWriter writer = new FileWriter(getDestinationFile("type", type.getName(), packageName));
        try {
            Template template = ve.getTemplate("Type.wm");
            template.merge(context, writer);
        } finally {
            writer.close();
        }
    }

    private void generateResponse(VelocityEngine ve, String api, String packageName) throws IOException {
        Template template;
        template = ve.getTemplate("Response.wm");
        VelocityContext context = new VelocityContext();
        context.put("api", api);
        context.put("package", packageName);
        FileWriter writer = new FileWriter(getDestinationFile("response", api + "Response", packageName));
        try {
            template.merge(context, writer);
        } finally {
            writer.close();
        }
    }
}
