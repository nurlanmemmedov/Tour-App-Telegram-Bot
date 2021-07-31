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
    @DisplayName("Translator get question - Az")
    void getQuestionAz(){
        List<Action> actions = new ArrayList<>();
        List<Translation> translations = new ArrayList<>();
        actions.add(Action.builder().type(ActionType.TEXT).answer("text").build());
        Question question = Question.builder().questionKey("test")
                .questionText("salam").regex(".*")
                .actions(actions).translations(translations).build();
        Assertions.assertEquals(Translator.getQuestion(question, "Az"), "salam");
    }


    @Test
    @DisplayName("Translator get question - En")
    void getQuestionEn(){
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
    @DisplayName("Translator get question - Ru")
    void getQuestionRu(){
        List<Action> actions = new ArrayList<>();
        List<Translation> translations = new ArrayList<>();
        actions.add(Action.builder().type(ActionType.TEXT).answer("text").build());
        translations.add(Translation.builder().language(Language.builder()
                .code("Ru").build()).text("Привет").build());
        Question question = Question.builder().questionKey("test")
                .questionText("salam").regex(".*")
                .actions(actions).translations(translations).build();
        Assertions.assertEquals(Translator.getQuestion(question, "Ru"), "Привет");
    }

    @Test
    @DisplayName("Translator get action - Az")
    void getActionAz(){
        List<Translation> translations = new ArrayList<>();
        Action action = Action.builder().answer("cavab")
                .translations(translations).build();
        Assertions.assertEquals(Translator.getAction(action, "Az"), "cavab");
    }

    @Test
    @DisplayName("Translator get action - En")
    void getActionEn(){
        List<Translation> translations = new ArrayList<>();
        translations.add(Translation.builder().language(Language.builder()
                .code("En").build()).text("answer").build());
        Action action = Action.builder().answer("test")
                .translations(translations).build();
        Assertions.assertEquals(Translator.getAction(action, "En"), "answer");
    }


    @Test
    @DisplayName("Translator get action - Ru")
    void getActionRu(){
        List<Translation> translations = new ArrayList<>();
        translations.add(Translation.builder().language(Language.builder()
                .code("Ru").build()).text("отвечать").build());
        Action action = Action.builder().answer("test")
                .translations(translations).build();
        Assertions.assertEquals(Translator.getAction(action, "Ru"), "отвечать");
    }

}