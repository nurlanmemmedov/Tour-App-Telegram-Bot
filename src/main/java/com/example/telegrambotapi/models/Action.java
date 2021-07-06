package com.example.telegrambotapi.models;

import com.example.telegrambotapi.enums.ActionType;

public class Action {
    private int id;
    private Question question;
    private ActionType type;
    private String answer;
    private Question nextQuestion;
}
