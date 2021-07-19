package com.example.telegrambotapi.utils;

import com.example.telegrambotapi.models.entities.Action;
import com.example.telegrambotapi.models.entities.Translation;
import com.example.telegrambotapi.models.entities.Question;

public class Translator {
    public static String getQuestion(Question question, String code){
        Translation translation =  question.getTranslations().stream()
                .filter(t -> t.getLanguage().getCode().equals(code))
                .findFirst().orElse(null);
        if (translation == null) return question.getQuestionText();
        return translation.getText();
    }

    public static String getAction(Action action, String code){
        Translation translation =  action.getTranslations().stream()
                .filter(t -> t.getLanguage().getCode().equals(code))
                .findFirst().orElse(null);
        if (translation == null) return action.getAnswer();
        return translation.getText();
    }

}
