package com.salesforce.revcloud;

import com.salesforce.revcloud.client.AuthClient;
import com.salesforce.revcloud.client.EventClient;
import org.apache.log4j.Logger;
import org.cometd.bayeux.client.ClientSessionChannel;

import java.net.MalformedURLException;
import java.net.URL;

public class ApiContext {

    private AuthClient authClient;
    private EventClient eventClient;
    private String loginEndpoint = "https://login.salesforce.com";
    private long eventListenerTimeout = 15000;
    private String apiVersion = "55.0";
    private boolean throwOnEventTimeout = true;
    private boolean shouldInitEventClient = false;

    private Logger LOGGER = Logger.getLogger(ApiContext.class);

    public ApiContext() {
    }

    /**
     * Construct an API context with session id, server URL and api version
     */
    public ApiContext(String sessionId, String serverUrl, String apiVersion) throws Exception {
        authClient = new AuthClient(sessionId, serverUrl, apiVersion);
        initEventClient();
    }

    public void init(String loginEndpoint, String username, String password, String apiVersion, boolean initEventClient) throws Exception {
        setLoginEndpoint(loginEndpoint);
        this.init(username, password, apiVersion, initEventClient);
    }

    public void init(String loginEndpoint, String username, String password, String apiVersion) throws Exception {
        setLoginEndpoint(loginEndpoint);
        this.init(username, password, apiVersion, true);
    }

    public void init(String username, String password, String apiVersion) throws Exception {
        init(username, password, apiVersion, true);
    }

    private void init(String username, String password, String apiVersion, boolean initEventClient) throws Exception {
        this.apiVersion = apiVersion;
        authClient = new AuthClient(apiVersion);
        authClient.login(getLoginEndpoint(), username, password);
        this.shouldInitEventClient = initEventClient;
        if (initEventClient) {
            initEventClient();
        }
    }

    public void close() {
        if (eventClient != null) {
            eventClient.disconnect();
        }
    }

    public boolean isInitiated() {
        return this.authClient != null && this.authClient.isAuthenticated() &&
                (!shouldInitEventClient || (eventClient != null && eventClient.isConnected()));
    }

    public String getLoginEndpoint() {
        return loginEndpoint;
    }

    public void setLoginEndpoint(String loginEndpoint) {
        this.loginEndpoint = loginEndpoint;
    }

    public long getEventListenerTimeout() {
        return eventListenerTimeout;
    }

    public void setEventListenerTimeout(long eventListenerTimeout) {
        this.eventListenerTimeout = eventListenerTimeout;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getSessionId() {
        return authClient.getSessionId();
    }

    public String getServiceUrl() {
        return authClient.getServiceUrl();
    }

    public boolean isThrowOnEventTimeout() {
        return throwOnEventTimeout;
    }

    public void setThrowOnEventTimeout(boolean throwOnEventTimeout) {
        this.throwOnEventTimeout = throwOnEventTimeout;
    }

    public String buildFullApiUrl(String apiEndpoint) {
        return getServiceUrl() + "/services/data/v" + getApiVersion() + apiEndpoint;
    }

    public void subscribe(String eventType, ClientSessionChannel.MessageListener listener) {
        eventClient.subscribe("/event/" + eventType, listener);
        LOGGER.debug("Subscribed to " + eventType);
    }

    private void initEventClient() throws InterruptedException {
        eventClient = new EventClient();
        Thread t = new Thread(() -> {
            try {
                eventClient.connect(authClient.getEventReplayEndpoint(), authClient.getSessionId());
                notify();
            } catch (Exception e) {
                LOGGER.error("Error connecting to event bus", e);
            }
        });
        t.start();
        synchronized (t) {
            t.wait(10000);
        }
        LOGGER.info("EventClient connected, ready for subscription");
    }
}
