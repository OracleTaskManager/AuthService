package com.Oracle.Project.data;

public record UserRegister(
        String name,
        String email,
        String password,
        Work_Mode work_mode,
        Long telegram_chat_id,
        String role
) {
}
