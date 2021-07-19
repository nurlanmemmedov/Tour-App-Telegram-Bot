package com.example.telegrambotapi.utils;

import com.example.telegrambotapi.enums.ActionType;
import com.example.telegrambotapi.models.entities.Action;
import com.example.telegrambotapi.models.entities.Language;
import com.example.telegrambotapi.models.entities.Translation;
import com.example.telegrambotapi.models.entities.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TranslatorTest {
    @Test
    @DisplayName("Test should pass when question translation is equals to hello in english")
    void getQuestion(){
        List<Action> actions = new ArrayList<>();
        List<Translation> translations = new ArrayList<>();
        actions.add(Action.builder().type(ActionType.TEXT).answer("text").build());
        translations.add(Translation.builder().language(Language.builder()
                .code("En").build()).text("hello").build());
        Question question = Question.builder().questionKey("test")
                .questionText("salam").regex(".*")
                .actions(actions).translations(translations).build();
        Assertions.assertEquals(Translator.getQuestion(question, "En"), "hello");
    }

    @Test
    @DisplayName("Test should pass when action translation is equals to hello in english")
    void getAction(){
        List<Translation> translations = new ArrayList<>();
        translations.add(Translation.builder().language(Language.builder()
                .code("En").build()).text("hello").build());
        Action action = Action.builder().answer("test")
                .translations(translations).build();
        Assertions.assertEquals(Translator.getAction(action, "En"), "hello");
    }

}