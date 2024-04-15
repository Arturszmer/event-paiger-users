package com.eventpaiger;

import com.eventpaiger.containers.PostgresSQLTestContainer;
import com.eventpaiger.containers.GeocodingMockServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.JdbcDatabaseContainer;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("integrationTest")
public abstract class BaseIntegrationTestSettings {

    protected static final String BASIC_PATH = "/api/v1";
    private static final JdbcDatabaseContainer<?> POSTGRES_CONTAINER = PostgresSQLTestContainer.createServer();
    private static final WireMockServer REST_TEMPLATE_SERVER = GeocodingMockServer.createServer();

    protected static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    protected MockMvc mockMvc;

    static {
        POSTGRES_CONTAINER.start();
        REST_TEMPLATE_SERVER.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    protected ResultActions getRequest(String url,
                                       MultiValueMap<String, String> params,
                                       MediaType mediaType) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(BASIC_PATH + url)
                .params(params)
                .contentType(mediaType));
    }

    protected ResultActions getRequest(String url,
                                       MultiValueMap<String, String> params) throws Exception {
        return getRequest(url, params, MediaType.APPLICATION_JSON);
    }

    protected ResultActions postRequest(String url,
                                        String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(BASIC_PATH + url)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));
    }

    protected ResultActions putRequest(String url,
                                       String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .put(BASIC_PATH + url)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));
    }
}
