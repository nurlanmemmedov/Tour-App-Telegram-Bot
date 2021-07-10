package com.example.telegrambotapi.utils;

import com.example.telegrambotapi.enums.ActionType;
import com.example.telegrambotapi.models.entities.Question;

public class Validator {
    public static boolean validate(Question question, String text){
        if (question == null) return false;
        boolean hasButton = question.getActions().stream()
                .anyMatch(a -> a.getType() == ActionType.BUTTON);
        if (hasButton){
            return  question.getActions()
                    .stream().anyMatch(a ->
                        (a.getAnswer().equals(text) || a.getActionTranslations()
                                .stream().anyMatch(t -> t.getText().equals(text))
                    ));
        }
        return text.matches(question.getRegex());
    }
}
