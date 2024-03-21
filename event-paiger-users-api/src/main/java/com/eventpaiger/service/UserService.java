package com.eventpaiger.service;

import com.eventpaiger.auth.ChangePasswordRequest;
import com.eventpaiger.dto.SimpleAddressDto;
import com.eventpaiger.dto.userprofile.UserProfileDetailsDto;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;

public interface UserService {

    void changePassword(ChangePasswordRequest request, Principal principal) throws UserPrincipalNotFoundException;
    UserProfileDetailsDto updateAddress(SimpleAddressDto userUpdateAddress);
}
