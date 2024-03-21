package com.eventpaiger.auth;

public record AuthenticationRequest(
        String usernameOrEmail,
        String password
) {
}
