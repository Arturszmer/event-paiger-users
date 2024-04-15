package com.eventpaiger.containers;

import com.eventpaiger.AbstractWireMockServer;
import com.github.tomakehurst.wiremock.WireMockServer;

import static com.eventpaiger.containers.MockServerUtils.get200;

public class GeocodingMockServer extends AbstractWireMockServer {

    private static final String body = "[{\"place_id\":189468021,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":194458333,\"lat\":\"52.224788\",\"lon\":\"21.0371975\",\"class\":\"highway\",\"type\":\"residential\",\"place_rank\":26,\"importance\":0.10000999999999993,\"addresstype\":\"road\",\"name\":\"Koźmińska\",\"display_name\":\"Koźmińska, Solec, Śródmieście, Warszawa, województwo mazowieckie, 00-448, Polska\",\"boundingbox\":[\"52.2243653\",\"52.2252996\",\"21.0370465\",\"21.0373262\"]},{\"place_id\":190602600,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":30830859,\"lat\":\"52.2267115\",\"lon\":\"21.0359566\",\"class\":\"highway\",\"type\":\"residential\",\"place_rank\":26,\"importance\":0.10000999999999993,\"addresstype\":\"road\",\"name\":\"Koźmińska\",\"display_name\":\"Koźmińska, Solec, Śródmieście, Warszawa, województwo mazowieckie, 00-430, Polska\",\"boundingbox\":[\"52.2266425\",\"52.2270223\",\"21.0356800\",\"21.0360023\"]},{\"place_id\":188829728,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":983718901,\"lat\":\"52.2242841\",\"lon\":\"21.0373493\",\"class\":\"highway\",\"type\":\"residential\",\"place_rank\":26,\"importance\":0.10000999999999993,\"addresstype\":\"road\",\"name\":\"Koźmińska\",\"display_name\":\"Koźmińska, Solec, Śródmieście, Warszawa, województwo mazowieckie, 00-452, Polska\",\"boundingbox\":[\"52.2237751\",\"52.2243653\",\"21.0373262\",\"21.0374944\"]},{\"place_id\":189494936,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":102694348,\"lat\":\"52.2261056\",\"lon\":\"21.0364564\",\"class\":\"highway\",\"type\":\"residential\",\"place_rank\":26,\"importance\":0.10000999999999993,\"addresstype\":\"road\",\"name\":\"Koźmińska\",\"display_name\":\"Koźmińska, Solec, Śródmieście, Warszawa, województwo mazowieckie, 00-435, Polska\",\"boundingbox\":[\"52.2252996\",\"52.2266425\",\"21.0360023\",\"21.0370465\"]}]";

    public static WireMockServer createServer(){
        new GeocodingMockServer();
        get200(wireMockServer, ".*", body);
        return wireMockServer;
    }
}
