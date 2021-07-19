package com.example.telegrambotapi.utils;

import com.example.telegrambotapi.enums.ActionType;
import com.example.telegrambotapi.models.entities.Action;
import com.example.telegrambotapi.models.entities.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ValidatorTest {

    @Test
    @DisplayName("Test should pass when question text is valid")
    void validate(){
        List<Action> actions = new ArrayList<>();
        actions.add(Action.builder().type(ActionType.TEXT).answer("text").build());
        Question question = Question.builder().questionKey("test")
                .questionText("test").regex("[0-9]+")
                .actions(actions).build();
        Assertions.assertFalse(Validator.validateQuestion(question, "123a"));
    }
}