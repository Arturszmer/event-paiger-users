package com.eventpaiger.dto;

import java.util.UUID;

public record UserProfileDto(
        String username,
        String email,
        UUID eventOrganizerId
) {
}
