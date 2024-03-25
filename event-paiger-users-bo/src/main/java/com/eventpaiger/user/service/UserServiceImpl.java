package com.eventpaiger.user.service;

import com.eventpaiger.auth.ChangePasswordRequest;
import com.eventpaiger.dto.SimpleAddressDto;
import com.eventpaiger.dto.userprofile.UserProfileDetailsDto;
import com.eventpaiger.service.UserService;
import com.eventpaiger.user.assembler.UserProfileAssembler;
import com.eventpaiger.user.model.user.SimpleAddress;
import com.eventpaiger.user.model.user.UserProfile;
import com.eventpaiger.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.eventpaiger.user.helper.SecurityContextUsers;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserProfileRepository repository;
    private final SecurityContextUsers securityContextUsers;

    @Override
    public void changePassword(ChangePasswordRequest request, Principal principal) {
        String username = principal.getName();

        UserProfile user = repository.findUserProfileByUsername(username)
                .orElseThrow(() ->  new UsernameNotFoundException(String.format("User %s is not found", username)));

        isCurrentPasswordCorrect(request.currentPassword(), user);
        isNewPasswordMatches(request.newPassword(), request.confirmationPassword());

        user.setNewPassword(passwordEncoder.encode(request.newPassword()));

        repository.save(user);
        log.info("Password for user: {} has been changed", user.getUsername());
    }

    @Override
    public UserProfileDetailsDto updateAddress(SimpleAddressDto userUpdateAddress) {

        UserProfile user = securityContextUsers.getUserProfileFromAuthentication();
        UserProfile savedUser = updateUserAddressIfIsChanged(userUpdateAddress, user);

        return UserProfileAssembler.toDtoDetails(savedUser);
    }

    private UserProfile updateUserAddressIfIsChanged(SimpleAddressDto userAddressDto, UserProfile userProfile) {
        if(userAddressDto == null){
            return userProfile;
        }
        SimpleAddress changedAddress = UserProfileAssembler.toEntity(userAddressDto);

        if(isUserAddressIsReallyChanged(changedAddress, userProfile)){
            userProfile.setUserAddress(changedAddress);
            return repository.save(userProfile);
        }

        return userProfile;
    }

    private boolean isUserAddressIsReallyChanged(SimpleAddress changedAddress, UserProfile userProfile) {
        return userProfile.getUserAddress() != null && !userProfile.getUserAddress().equals(changedAddress);
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
