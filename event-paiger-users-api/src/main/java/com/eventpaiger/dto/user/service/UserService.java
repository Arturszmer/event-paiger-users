package com.eventpaiger.dto.user.service;

import com.eventpaiger.dto.auth.ChangePasswordRequest;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;

public interface UserService {

    void changePassword(ChangePasswordRequest request, Principal principal) throws UserPrincipalNotFoundException;
}
