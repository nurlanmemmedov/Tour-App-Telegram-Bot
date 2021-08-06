package com.example.telegrambotapi.models;

import com.example.telegrambotapi.models.entities.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session implements Serializable {
    private String uuid;
    private Integer clientId;
    private Long chatId;
    private Map<String, String> data;
    private Question currentQuestion;
    private String userLanguage;
    private LocalDate calendarMonth;
}
