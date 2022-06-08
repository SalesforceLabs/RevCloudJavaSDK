package com.salesforce.api.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.salesforce.revcloud.ApiContext;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class GenerateAPI {

    private String loginEndpoint = "https://www.salesforce.com";
    private String username;
    private String password;
    private String appVersion = "55.0";
    protected String destination = Paths.get("src", "main", "java").toAbsolutePath().toString();
    protected String packageName;

    public static void main(String[] args) throws Exception {
        GenerateAPI generator = new GenerateAPI(args);
        generator.generateFromManifest();
    }

    private GenerateAPI(String[] args) throws Exception {
        try {
            for (int i = 0; i < args.length; i++) {
                if ("-host".equals(args[i]) || "-h".equals(args[i])) {
                    loginEndpoint = args[i + 1];
                } else if ("-user".equals(args[i]) || "-u".equals(args[i])) {
                    username = args[i + 1];
                } else if ("-password".equals(args[i]) || "-p".equals(args[i])) {
                    password = args[i + 1];
                } else if ("-version".equals(args[i]) || "-v".equals(args[i])) {
                    appVersion = args[i + 1];
                } else if ("-package".equals(args[i]) || "-pkg".equals(args[i])) {
                    packageName = args[i + 1];
                }
            }
            if (username == null || password == null || packageName == null) {
                throw new IllegalArgumentException();
            }

        } catch (Exception e) {
            System.out.println("Usage: ApiGenerator [-host <login endpoint>] -user <username> -password <password> [-version <api version>] -package <package name>");
            System.exit(1);
        }
    }

    void generateFromManifest() throws Exception {
        ApiContext apiContext = new ApiContext();
        apiContext.init(this.loginEndpoint, this.username, this.password, this.appVersion, false);
        RamlToApiGenerator ramlGenerator = new RamlToApiGenerator(apiContext, this.packageName, this.destination);
        ActionToApiGenerator actionGenerator = new ActionToApiGenerator(apiContext, this.packageName, this.destination);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        URL manifestResource = this.getClass().getClassLoader().getResource("manifest.yaml");
        List<ManifestEntry> manifestEntries = mapper.readValue(manifestResource,  new TypeReference<List<ManifestEntry>>() { });
        System.out.println(manifestEntries);
        for (ManifestEntry api : manifestEntries) {
            System.out.println("Generating API for " + api);
            BaseApiGenerator generator = api.getRaml() != null ? ramlGenerator : actionGenerator;
            generator.generate(api);
        }
    }
}
