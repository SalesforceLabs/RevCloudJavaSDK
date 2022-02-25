package com.salesforce.revcloud.client;

import org.apache.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.ByteBufferContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ApiClient extends ProxiedClient {

    private static final Logger LOGGER = Logger.getLogger(ApiClient.class);

    /**
     * Invoke an API then callback on the handler function for verification
     * @param endpointUrl
     * @param sessionId
     * @param method
     * @param payload
     * @return ContentResponse
     * @throws IOException
     */
    public ContentResponse invokeApi(String endpointUrl, String sessionId, HttpMethod method, String payload) throws Exception {
        HttpClient httpClient = new HttpClient(new SslContextFactory());
        configProxy(httpClient);
        try {
            httpClient.start();
            URL endpoint = new URL(endpointUrl);
            Request request = httpClient.newRequest(endpoint.toURI());
            request.method(method);
            request.header("Authorization", "Bearer " + sessionId);
            request.header("Content-Type", "application/json");
            if (HttpMethod.POST.equals(method)) {
                LOGGER.debug("API request payload: " + payload);
                request.content(new ByteBufferContentProvider("application/json", ByteBuffer.wrap(payload.getBytes(StandardCharsets.UTF_8))));
            }
            return request.send();
        } finally {
            httpClient.stop();
            httpClient.destroy();
        }
    }
}
