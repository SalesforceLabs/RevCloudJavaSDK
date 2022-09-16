package com.salesforce.api.generator;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.revcloud.ApiContext;
import com.salesforce.revcloud.client.ApiClient;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class BaseApiGenerator {

    protected String destination = "/tmp";
    private String packageName;
    private ApiContext apiContext;

    public BaseApiGenerator(ApiContext apiContext, String packageName, String destination) {
        this.apiContext = apiContext;
        this.packageName = packageName;
        this.destination = destination;
    }

    protected String getDestinationFile(String packageSuffix, String fileName) throws IOException {
        return getDestinationFile(packageSuffix, fileName, this.packageName);
    }

    protected String getDestinationFile(String packageSuffix, String fileName, String packageName) throws IOException {
        String rootPath = this.destination + "/" + packageName.replaceAll("\\.", "/") + "/" + packageSuffix.replaceAll("\\.", "/");
        File rootDir = new File(rootPath).getCanonicalFile();
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        return rootPath + "/" + fileName + ".java";
    }

    protected VelocityEngine getVelocityEngine() {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER_PATHS, this.getClass().getClassLoader().getResource(".").getFile());
        ve.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        ve.init();
        return ve;
    }

    void generate(ManifestEntry manifestEntry) throws Exception {
        VelocityEngine ve = getVelocityEngine();
        Template template;
        VelocityContext context;
        FileWriter writer;

        String packageName = manifestEntry.getPackageName() != null ? manifestEntry.getPackageName() : this.packageName;

        Event eventModel = null;
        if (manifestEntry.getEvent() != null) {
            eventModel = describe("/sobjects/" + manifestEntry.getEvent() + "/eventSchema", Event.class);
            // Generate event class(es)
            generateEventClasses(ve, eventModel, packageName);
        }
        generateInternal(manifestEntry, ve, packageName, eventModel);
    }

    private void generateEventClasses(VelocityEngine ve, Event eventModel, String packageName) throws IOException {
        Template template;
        FileWriter writer;
        VelocityContext context;
        template = ve.getTemplate("Event.wm");
        context = new VelocityContext();
        context.put("package", packageName);
        context.put("event", eventModel);
        writer = new FileWriter(getDestinationFile("event", eventModel.getClassName()));
        try {
            template.merge(context, writer);
        } finally {
            writer.close();
        }
        for (EventField field : eventModel.getFields()) {
            if (field.isComplex() && field.getFields() != null) {
                generateEventClasses(ve, field, packageName);
            }
        }
    }

    abstract protected void generateInternal(ManifestEntry manifestEntry, VelocityEngine ve, String packageName, Event eventModel) throws Exception;

    protected <T> T describe(String apiEndpoint, Class<T> clazz) throws Exception {
        try {
            ApiClient client = new ApiClient();
            ContentResponse response = client.invokeApi(apiContext.buildFullApiUrl(apiEndpoint), apiContext.getSessionId(), HttpMethod.GET, null);
            byte[] descResult = response.getContent();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.findAndRegisterModules();
            return (T)mapper.readValue(new String(descResult), clazz);
        } finally {
            apiContext.close();
        }
    }
}
