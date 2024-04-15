package com.eventpaiger.auth;

public record RegistrationRequest(
        String username,
        String email,
        String password,
        boolean isOrganizer
) {
}
