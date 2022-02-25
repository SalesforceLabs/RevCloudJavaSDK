package com.salesforce.revcloud.client;

import org.apache.log4j.Logger;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EventClient extends ProxiedClient {

    private HttpClient httpClient;
    private BayeuxClient bayeuxClient;

    private Logger LOGGER = Logger.getLogger(EventClient.class);

    private boolean connected;

    private Map<String, ClientSessionChannel> channels = new HashMap<>();

    public EventClient() {
    }
    
    public boolean isConnected() {
        return this.connected;
    }

    public synchronized void connect(URL replayEndpoint, String sessionId) throws Exception {
        if (!this.connected) {
            httpClient = new HttpClient(new SslContextFactory());
            configProxy(httpClient);
            httpClient.start();
            ClientTransport transport = new LongPollingTransport(new HashMap<>(), httpClient) {
                @Override
                protected void customize(Request request) {
                    request.header("Authorization", "Bearer " + sessionId);
                }
            };
            bayeuxClient = new BayeuxClient(replayEndpoint.toExternalForm(), transport);
            bayeuxClient.handshake();
            bayeuxClient.waitFor(10000, BayeuxClient.State.HANDSHAKEN);
            this.connected = true;
        }
    }

    public void disconnect() {
        try {
            if (bayeuxClient != null) {
                bayeuxClient.disconnect();
            }
            if (httpClient != null) {
                httpClient.stop();
                httpClient.destroy();
            }
        } catch (Exception e) {}
    }

    public boolean subscribe(String topic, ClientSessionChannel.MessageListener listener) {
        ClientSessionChannel channel = channels.get(topic);
        if (channel == null) {
            channel = bayeuxClient.getChannel(topic);
            channels.put(topic, channel);
        }
        return channel.subscribe(listener);
    }

    public void unsubscribe(String topic, ClientSessionChannel.MessageListener listener) {
        ClientSessionChannel channel = channels.get(topic);
        if (channel == null) {
            throw new IllegalArgumentException("Channel " + topic + " doesn't exist");
        }
        channel.unsubscribe(listener);
    }
}
