package com.Oracle.AuthService.data;

public record UpdatePasswordRequest(
        String email,
        String newPassword
) {
}
