package com.eventpaiger.dto.userprofile;

import com.eventpaiger.dto.SimpleAddressDto;

import java.util.UUID;

public record UserProfileDetailsDto(
        String username,
        String email,
        UUID eventOrganizerId,
        String firstName,
        String lastName,
        SimpleAddressDto userAddressDto
) {
}
