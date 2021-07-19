package com.example.telegrambotapi.utils;

import com.example.telegrambotapi.enums.ActionType;
import com.example.telegrambotapi.models.entities.Action;
import com.example.telegrambotapi.models.entities.ActionTranslation;
import com.example.telegrambotapi.models.entities.Question;
import com.example.telegrambotapi.models.entities.QuestionTranslation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TranslatorTest {
    @Test
    @DisplayName("Test should pass when question translation is equals to hello in english")
    void getQuestion(){
        List<Action> actions = new ArrayList<>();
        List<QuestionTranslation> translations = new ArrayList<>();
        actions.add(Action.builder().type(ActionType.TEXT).answer("text").build());
        translations.add(QuestionTranslation.builder().code("en").text("hello").build());
        Question question = Question.builder().questionKey("test")
                .questionText("salam").regex(".*")
                .actions(actions).questionTranslations(translations).build();
        Assertions.assertEquals(Translator.getQuestion(question, "en"), "hello");
    }

    @Test
    @DisplayName("Test should pass when action translation is equals to hello in english")
    void getAction(){
        List<ActionTranslation> translations = new ArrayList<>();
        translations.add(ActionTranslation.builder().code("en").text("hello").build());
        Action action = Action.builder().answer("test")
                .actionTranslations(translations).build();
        Assertions.assertEquals(Translator.getAction(action, "en"), "hello");
    }

}