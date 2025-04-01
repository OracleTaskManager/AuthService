package com.Oracle.AuthService.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserRegister(
        @NotBlank(message = "El nombre es requerido")
        String name,

        @NotBlank(message = "El correo es requerido")
        @Email(message = "El formato del correo es inválido")
        String email,

        @NotBlank(message = "La contraseña es requerida")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{12,}$",
                message = "La contraseña debe tener al menos 12 caracteres, incluir una mayúscula, una minúscula, un dígito, y un carácter especial, y no tener espacios"
        )
        String password,

        WorkMode workMode,
        Long telegramChatId,
        String role
) {
}
