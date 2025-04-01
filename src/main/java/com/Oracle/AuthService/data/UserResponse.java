package com.Oracle.AuthService.data;

public record UserResponse(
        Long userId,
        String name,
        String email,
        String role,
        String workMode
) {
}
