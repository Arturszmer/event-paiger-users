package com.eventpaiger.containers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.MediaType;

public class MockServerUtils {

    private static final String MEDIA_TYPE = "Content-Type";

    static void get200(WireMockServer wireMockServer, String url, String body){
        wireMockServer.givenThat(WireMock.get(WireMock.urlPathMatching(url))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(MEDIA_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(body))
        );
    }
}
