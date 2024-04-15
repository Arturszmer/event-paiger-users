package com.eventpaiger.controller;

import com.eventpaiger.BaseIntegrationTestSettings;
import com.eventpaiger.auth.AuthenticationResponse;
import com.eventpaiger.auth.RegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends BaseIntegrationTestSettings {

    @Test
    public void shouldReturn401WhenUserIsNotAuthenticated() throws Exception {
        // given
        mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
        getRequest("/", new LinkedMultiValueMap<>()).andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldRegisterNewUser() throws Exception {
        // given
        RegistrationRequest request = new RegistrationRequest("user-auth-controller", "user@eventpaiger.pl", "password", false);
        String body = mapper.writeValueAsString(request);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/register")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        AuthenticationResponse authenticationResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(), AuthenticationResponse.class);

        // then
        assertNotNull(authenticationResponse.accessToken());
        assertEquals(200, mvcResult.getResponse().getStatus());
    }
}
