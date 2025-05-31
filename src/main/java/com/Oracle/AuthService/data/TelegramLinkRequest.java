package com.Oracle.AuthService.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TelegramLinkRequest(
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotNull Long chatId
) {
}
