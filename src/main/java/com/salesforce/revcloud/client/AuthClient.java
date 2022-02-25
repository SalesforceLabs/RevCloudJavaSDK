package com.salesforce.revcloud.client;

import org.apache.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.ByteBufferContentProvider;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

public class AuthClient extends ProxiedClient {

    private static final String SERVICES_SOAP_PARTNER_ENDPOINT = "/services/Soap/u/";
    private static final String ENV_END = "</soapenv:Body></soapenv:Envelope>";
    private static final String ENV_START = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' "
            + "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
            + "xmlns:urn='urn:partner.soap.sforce.com'><soapenv:Body>";
    private static final Logger LOGGER = Logger.getLogger(ApiClient.class);

    private String sessionId;
    private String serverUrl;
    private boolean authenticated = false;
    private String apiVersion;

    public AuthClient(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public AuthClient(String sessionId, String serverUrl, String apiVersion) {
        if (sessionId == null || serverUrl == null || apiVersion == null) {
            throw new IllegalArgumentException("Missing sessionId, serverUrl or apiVersion");
        }
        this.sessionId = sessionId;
        this.serverUrl = serverUrl;
        this.apiVersion = apiVersion;
        // trust the given sessionId and serverUrl
        this.authenticated = true;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public synchronized void login(String loginEndpoint, String username, String password) throws Exception {
        HttpClient httpClient = new HttpClient(new SslContextFactory());
        configProxy(httpClient);
        try {
            httpClient.start();
            URL endpoint = new URL(new URL(loginEndpoint), SERVICES_SOAP_PARTNER_ENDPOINT + apiVersion + "/");
            Request post = httpClient.POST(endpoint.toURI());
            post.content(new ByteBufferContentProvider("text/xml", ByteBuffer.wrap(soapXmlForLogin(username, password))));
            post.header("SOAPAction", "''");
            post.header("PrettyPrint", "Yes");
            ContentResponse response = post.send();
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            spf.setNamespaceAware(true);
            SAXParser saxParser = spf.newSAXParser();
            AuthClient.LoginResponseParser parser = new AuthClient.LoginResponseParser();
            saxParser.parse(new ByteArrayInputStream(response.getContent()), parser);
            if (parser.sessionId == null) {
                throw new RuntimeException("Unable to authenticate with endpoint, please check the endpoint URL, user name and password");
            }
            this.sessionId = parser.sessionId;
            URL soapEndpoint = new URL(parser.serverUrl);
            this.serverUrl = new URL(soapEndpoint.getProtocol(), soapEndpoint.getHost(), soapEndpoint.getPort(), "").toExternalForm();
            LOGGER.info("Login successfully into " + loginEndpoint);
            authenticated = true;
        } finally {
            httpClient.stop();
            httpClient.destroy();
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getServiceUrl() {
        return serverUrl;
    }

    public URL getEventReplayEndpoint() throws MalformedURLException {
        return new URL(serverUrl + "/cometd/" + apiVersion);
    }

    private byte[] soapXmlForLogin(String username, String password) throws UnsupportedEncodingException {
        return (ENV_START + "  <urn:login>" + "    <urn:username>" + username + "</urn:username>" + "    <urn:password>"
                + password + "</urn:password>" + "  </urn:login>" + ENV_END).getBytes("UTF-8");
    }

    private static class LoginResponseParser extends DefaultHandler {

        private String buffer;
        private String faultstring;

        private boolean reading = false;
        private String serverUrl;
        private String sessionId;

        @Override
        public void characters(char[] ch, int start, int length) {
            if (reading) buffer = new String(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            reading = false;
            switch (localName) {
                case "sessionId":
                    sessionId = buffer;
                    break;
                case "serverUrl":
                    serverUrl = buffer;
                    break;
                case "faultstring":
                    faultstring = buffer;
                    break;
                default:
            }
            buffer = null;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            switch (localName) {
                case "sessionId":
                    reading = true;
                    break;
                case "serverUrl":
                    reading = true;
                    break;
                case "faultstring":
                    reading = true;
                    break;
                default:
            }
        }
    }
}
