package com.example.telegrambotapi.utils;

import com.example.telegrambotapi.enums.ActionType;
import com.example.telegrambotapi.models.entities.Question;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Validator {

    public static boolean validateMessage(Message message){
        if (message == null) return false;
        if (message != null && !message.hasText()){
            return false;
        }
        return true;
    }

    public static boolean validateQuestion(Question question, String text){
        if (question == null) return false;
        if (question.getRegex() == null) return true;
        if (question.getActions() == null) return false;
            Boolean hasButton = question.getActions().stream()
                .anyMatch(a -> a.getType() == ActionType.BUTTON);
        if (hasButton){
            return  question.getActions()
                    .stream().anyMatch(a ->
                        (a.getAnswer().equals(text) || a.getTranslations()
                                .stream().anyMatch(t -> t.getText().equals(text))
                    ));
        }
        return text.matches(question.getRegex());
    }
}
