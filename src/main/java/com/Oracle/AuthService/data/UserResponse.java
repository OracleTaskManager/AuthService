package com.Oracle.AuthService.data;

public record UserResponse(
        Long user_id,
        String name,
        String email,
        String role,
        String work_Mode
) {
}
