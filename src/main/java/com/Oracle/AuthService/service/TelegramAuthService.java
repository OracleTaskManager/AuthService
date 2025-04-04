package com.Oracle.AuthService.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TelegramAuthService {

    @Value("${telegram.bot.secret}")
    private String validBotSecret;

    public boolean validateRequest(String incomingSecret){
        return validBotSecret != null && incomingSecret != null && validBotSecret.equals(incomingSecret);
    }

}
