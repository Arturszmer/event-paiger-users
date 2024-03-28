package com.eventpaiger.controller;

import com.eventpaiger.BaseIntegrationTestSettings;
import com.eventpaiger.auth.ChangePasswordRequest;
import com.eventpaiger.auth.RegistrationRequest;
import com.eventpaiger.authentication.AuthenticationService;
import com.eventpaiger.dto.EventAddressDto;
import com.eventpaiger.dto.SimpleAddressDto;
import com.eventpaiger.dto.userprofile.UserProfileDetailsDto;
import com.eventpaiger.user.model.user.UserProfile;
import com.eventpaiger.user.repository.UserProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Sql(scripts = "/sql-init/user-profile-init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql-init/user-profile-clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class UserControllerTest extends BaseIntegrationTestSettings {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserProfileRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final String USER = "user";
    private static final String USER_EMAIL = "user@user.com";
    private static final String USER_ADDRESS_PATH = "/user/update-address";
    private static final String EVENT_ADDRESS_PATH = "/user/update-event-address";

    @Test
    @WithMockUser(password = "old-password")
    public void should_change_password() throws Exception {

        // given
        authenticationService.register(createUserProfileForTest());

        // when
        postRequest("/change-password", getBodyToPasswordChange());

        // then
        Optional<UserProfile> user = repository.findUserProfileByUsername(USER);
        assertTrue(user.isPresent());
        assertTrue(passwordEncoder.matches("new-password", user.get().getPassword()));
    }

    @Test
    @WithMockUser(username = "admin")
    public void should_update_user_address() throws Exception {
        // given
        SimpleAddressDto updatedAddress = getSimpleAddress();


        // when
        MvcResult mvcResult = putRequest(USER_ADDRESS_PATH,
                mapper.writeValueAsString(updatedAddress)).andReturn();
        UserProfileDetailsDto userProfileDetailsDto = mapper.readValue(mvcResult.getResponse()
                .getContentAsString(), UserProfileDetailsDto.class);

        // then
        assertNotNull(userProfileDetailsDto.userAddressDto());
        assertEquals(userProfileDetailsDto.userAddressDto(), updatedAddress);

    }

    @Test
    public void should_update_event_address() throws Exception {
        // given
        EventAddressDto userUpdateEventAddresses = getUserUpdateEventAddresses();


        // when
        MvcResult mvcResult = putRequest(EVENT_ADDRESS_PATH, mapper.writeValueAsString(userUpdateEventAddresses)).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());


        // then

    }

    private EventAddressDto getUserUpdateEventAddresses() {
        return new EventAddressDto(getSimpleAddress(), "Event Warszawa");
    }

    private SimpleAddressDto getSimpleAddress(){
        return new SimpleAddressDto(
                "Warszawa",
                "Warszawska",
                "00-001",
                "1",
                "5");
    }

    private SimpleAddressDto getCustomAddress(String city, String street){
        return new SimpleAddressDto(city, street,
                "00-001", "1", "5");
    }

    private RegistrationRequest createUserProfileForTest() {
        return new RegistrationRequest(USER, USER_EMAIL, "old-password", false);
    }

    private String getBodyToPasswordChange() throws JsonProcessingException {
        return mapper.writeValueAsString(new ChangePasswordRequest("old-password",
                "new-password", "new-password"));
    }

}
