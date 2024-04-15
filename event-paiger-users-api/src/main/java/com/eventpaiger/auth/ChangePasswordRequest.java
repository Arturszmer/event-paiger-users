package com.eventpaiger.auth;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword,
        String confirmationPassword
) {
}
