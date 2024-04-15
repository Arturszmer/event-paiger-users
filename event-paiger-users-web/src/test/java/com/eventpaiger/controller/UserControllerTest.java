package com.eventpaiger.controller;

import com.eventpaiger.BaseIntegrationTestSettings;
import com.eventpaiger.auth.ChangePasswordRequest;
import com.eventpaiger.auth.RegistrationRequest;
import com.eventpaiger.authentication.AuthenticationService;
import com.eventpaiger.dto.EventAddressDto;
import com.eventpaiger.dto.SimpleAddressDto;
import com.eventpaiger.dto.userprofile.UserProfileDetailsDto;
import com.eventpaiger.dto.userprofile.UserProfileWithEventAddressesDto;
import com.eventpaiger.user.model.event.EventAddress;
import com.eventpaiger.user.model.user.UserProfile;
import com.eventpaiger.user.repository.EventAddressRepository;
import com.eventpaiger.user.repository.UserProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Sql(scripts = "/sql-init/user-profile-init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql-init/user-profile-clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class UserControllerTest extends BaseIntegrationTestSettings {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserProfileRepository repository;
    @Autowired
    private EventAddressRepository eventAddressRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String USER = "user";
    private static final String USER_EMAIL = "user@user.com";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String USER_ADDRESS_PATH = "/user/update-address";
    private static final String EVENT_ADDRESS_PATH = "/user/update-event-address";

    @Test
    @WithMockUser(password = "old-password")
    public void should_change_password() throws Exception {

        // given
        authenticationService.register(createUserProfileForTest());

        // when
        postRequest("/user/change-password", getBodyToPasswordChange());

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
    @WithMockUser(username = "admin")
    public void should_update_event_address() throws Exception {
        // given
        EventAddressDto userUpdateEventAddresses = getUserUpdateEventAddresses();

        // when
        MvcResult mvcResult = putRequest(EVENT_ADDRESS_PATH, mapper.writeValueAsString(userUpdateEventAddresses)).andReturn();
        UserProfileWithEventAddressesDto response = mapper.readValue(mvcResult.getResponse().getContentAsString(), UserProfileWithEventAddressesDto.class);

        // then
        Optional<UserProfile> userProfileByEmail = repository.findUserProfileByEmail(ADMIN_EMAIL);
        assertTrue(userProfileByEmail.isPresent());
        UUID eventOrganizerId = userProfileByEmail.get().getEventOrganizerId();
        List<EventAddress> allUserEventsAddresses = eventAddressRepository.findAllByEventOrganizerId(eventOrganizerId);
        assertEquals(1, allUserEventsAddresses.size());
        assertEquals(response.eventAddressDtos().get(0).addressDto().city(), allUserEventsAddresses.get(0).getAddress().getCity());
        assertEquals("Event Warszawa", response.eventAddressDtos().get(0).customUserAddressName());
        assertEquals(eventOrganizerId, response.userProfileDto().eventOrganizerId());
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

    private RegistrationRequest createUserProfileForTest() {
        return new RegistrationRequest(USER, USER_EMAIL, "old-password", false);
    }

    private String getBodyToPasswordChange() throws JsonProcessingException {
        return mapper.writeValueAsString(new ChangePasswordRequest("old-password",
                "new-password", "new-password"));
    }

}
