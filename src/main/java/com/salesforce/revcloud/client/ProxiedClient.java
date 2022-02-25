package com.salesforce.revcloud.client;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpProxy;

public abstract class ProxiedClient {

    void configProxy(HttpClient client) {
        String proxyHost = System.getProperty("proxyHost");
        String proxyPort = System.getProperty("proxyPort");
        if (proxyHost != null && proxyPort != null) {
            client.getProxyConfiguration().getProxies().add(new HttpProxy(proxyHost, Integer.parseInt(proxyPort)));
        }
    }
}
