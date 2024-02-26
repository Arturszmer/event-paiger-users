package com.eventpaiger;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.test.util.TestSocketUtils;

public abstract class AbstractWireMockServer {

    protected static String serverName;
    protected static int port;
    protected static WireMockServer wireMockServer;


    //TODO: used when microservices will come
    public AbstractWireMockServer() {
        serverName = this.getClass().getSimpleName().toUpperCase();
        port = TestSocketUtils.findAvailableTcpPort();
        System.setProperty(serverName, String.valueOf(port));
        wireMockServer = new WireMockServer(port);
    }
}
