package com.eventpaiger.containers;

import com.eventpaiger.AbstractWireMockServer;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ContentTypes;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;

public class GeocodingNominatinMockServer extends AbstractWireMockServer {

    private static final String URL_PATH_TO_GEOCODING = "/nominatim.openstreetmap.org/";

    public static WireMockServer createServer(){
        new GeocodingNominatinMockServer();
        configureServer(wireMockServer);
        return wireMockServer;
    }

    private static void configureServer(WireMockServer wireMockServer) {
        wireMockServer.stubFor(post(urlPathMatching(URL_PATH_TO_GEOCODING + ".*"))
                .willReturn(aResponse().withHeader(ContentTypes.CONTENT_TYPE, APPLICATION_JSON)
                        .withBody("")));
    }

}
