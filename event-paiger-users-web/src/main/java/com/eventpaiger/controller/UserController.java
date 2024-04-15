package com.eventpaiger.controller;

import com.eventpaiger.dto.EventAddressDto;
import com.eventpaiger.dto.SimpleAddressDto;
import com.eventpaiger.dto.userprofile.UserProfileDetailsDto;
import com.eventpaiger.auth.ChangePasswordRequest;
import com.eventpaiger.dto.userprofile.UserProfileWithEventAddressesDto;
import com.eventpaiger.service.EventAddressService;
import com.eventpaiger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EventAddressService eventAddressService;

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request,
                                            Principal connectedUser) throws UserPrincipalNotFoundException {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-address")
    public ResponseEntity<UserProfileDetailsDto> updateUserAddress(@RequestBody SimpleAddressDto userUpdateAddress){
        return ResponseEntity.ok(userService.updateAddress(userUpdateAddress));
    }

    @PutMapping("/update-event-address")
    public ResponseEntity<UserProfileWithEventAddressesDto> updateEventAddresses(@RequestBody @NonNull EventAddressDto eventAddresses){
        return ResponseEntity.ok(eventAddressService.updateEventAddresses(eventAddresses));
    }
}
