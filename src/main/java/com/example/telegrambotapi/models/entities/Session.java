package com.example.telegrambotapi.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    private String uuid;
    private Integer clientId;
    private Long chatId;
    private Map<String, String> data;
    private Question currentQuestion;
    private String userLanguage;
}
