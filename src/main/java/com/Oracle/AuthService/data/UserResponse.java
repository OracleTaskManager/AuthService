package com.Oracle.AuthService.data;

public record UserResponse(
        String name,
        String email,
        String role,
        String  work_Mode
) {
}
