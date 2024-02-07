package com.eventpaiger.dto.auth;

public record RegistrationRequest(
        String username,
        String email,
        String password,
        boolean isOrganizer
) {
}
