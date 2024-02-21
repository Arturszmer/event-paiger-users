package com.eventpaiger.user.service;

import com.eventpaiger.dto.auth.ChangePasswordRequest;
import com.eventpaiger.dto.user.service.UserService;
import com.eventpaiger.security.SecurityContextHelper;
import com.eventpaiger.user.model.UserProfile;
import com.eventpaiger.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserProfileRepository repository;

    @Override
    public void changePassword(ChangePasswordRequest request, Principal principal) throws UserPrincipalNotFoundException {
        UserProfile user = SecurityContextHelper.getUserFromAuthentication();

        isCurrentPasswordCorrect(request.currentPassword(), user);
        isNewPasswordMatches(request.newPassword(), request.confirmationPassword());

        user.setNewPassword(passwordEncoder.encode(request.newPassword()));

        repository.save(user);
        log.info("Password for user: {} has been changed", user.getUsername());
    }

    private void isNewPasswordMatches(String newPassword, String confirmationPassword) {
        if(!newPassword.equals(confirmationPassword)){
            throw new IllegalStateException("New password is not matched with confirmation password");
        }
    }

    private void isCurrentPasswordCorrect(String currentPassword, UserProfile user){
        if(!passwordEncoder.matches(currentPassword, user.getPassword())){
            throw new IllegalStateException("The current password is incorrect");
        }
    }
}
