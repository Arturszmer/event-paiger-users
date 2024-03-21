package com.eventpaiger.dto.userprofile;

import java.util.UUID;

public record UserProfileDto(
        String username,
        String email,
        UUID eventOrganizerId
) {
}
