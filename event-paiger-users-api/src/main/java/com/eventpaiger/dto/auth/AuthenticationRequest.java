package com.eventpaiger.dto.auth;

public record AuthenticationRequest(
        String usernameOrEmail,
        String password
) {
}
