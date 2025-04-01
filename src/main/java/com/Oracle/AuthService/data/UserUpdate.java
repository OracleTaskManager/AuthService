package com.Oracle.AuthService.data;

public record UserUpdate(
        String name,
        WorkMode workMode,
        Long telegramChatId
) {
}
