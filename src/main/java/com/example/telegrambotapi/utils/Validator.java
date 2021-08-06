package com.example.telegrambotapi.utils;

import com.example.telegrambotapi.enums.ActionType;
import com.example.telegrambotapi.models.entities.Question;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
        Boolean hasButton = question.getActions().stream().anyMatch(a -> a.getType() == ActionType.BUTTON);
        if (hasButton){
            return  question.getActions()
                    .stream().anyMatch(a ->
                        (a.getAnswer().equals(text) || a.getTranslations()
                                .stream().anyMatch(t -> t.getText().equals(text))));
        }
        boolean result = text.matches(question.getRegex());
        if (!result) return false;
        if(question.getQuestionKey().equals("travelStartDate")
                || question.getQuestionKey().equals("travelEndDate")){
            result = validateDate(text);
        }
        return result;
    }


    static boolean validateDate(String text){
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(text, format);
            if (date.isBefore(LocalDate.now())){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
