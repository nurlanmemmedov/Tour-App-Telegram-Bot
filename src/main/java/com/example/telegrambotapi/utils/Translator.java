package com.example.telegrambotapi.utils;

import com.example.telegrambotapi.models.entities.Action;
import com.example.telegrambotapi.models.entities.ActionTranslation;
import com.example.telegrambotapi.models.entities.Question;
import com.example.telegrambotapi.models.entities.QuestionTranslation;

public class Translator {
    public static String getQuestion(Question question, String code){
        QuestionTranslation translation =  question.getQuestionTranslations().stream()
                .filter(t -> t.getCode().equals(code))
                .findFirst().orElse(null);
        if (translation == null) return question.getQuestionText();
        return translation.getText();
    }

    public static String getAction(Action action, String code){
        ActionTranslation translation =  action.getActionTranslations().stream()
                .filter(t -> t.getCode().equals(code))
                .findFirst().orElse(null);
        if (translation == null) return action.getAnswer();
        return translation.getText();
    }

}
