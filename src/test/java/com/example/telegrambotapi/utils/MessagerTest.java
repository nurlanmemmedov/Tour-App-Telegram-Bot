package com.example.telegrambotapi.utils;

import com.example.telegrambotapi.models.entities.Action;
import com.example.telegrambotapi.models.entities.Language;
import com.example.telegrambotapi.models.entities.Translation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessagerTest {
    @Test
    @DisplayName("incorrectAnswer -> Az")
    void incorrectAnswerAz(){
        Assertions.assertEquals(Messager.incorrectAnswer("Az"), "Yanlış cavab!");
    }

    @Test
    @DisplayName("incorrectAnswer -> En")
    void incorrectAnswerEn(){
        Assertions.assertEquals(Messager.incorrectAnswer("En"), "Incorrect answer!");
    }

    @Test
    @DisplayName("incorrectAnswer -> Ru")
    void incorrectAnswerRu(){
        Assertions.assertEquals(Messager.incorrectAnswer("Ru"), "Неправильный ответ!");
    }

    @Test
    @DisplayName("incorrectAnswer -> Null")
    void incorrectAnswerNull(){
        Assertions.assertEquals(Messager.incorrectAnswer(null), "Yanlış cavab!");
    }

    @Test
    @DisplayName("incorrectCommand -> Az")
    void incorrectCommandAz(){
        Assertions.assertEquals(Messager.incorrectCommand("Az"), "Yanlış komanda");
    }

    @Test
    @DisplayName("incorrectCommand -> En")
    void incorrectCommandEn(){
        Assertions.assertEquals(Messager.incorrectCommand("En"), "Incorrect command");
    }

    @Test
    @DisplayName("incorrectCommand -> Ru")
    void incorrectCommandRu(){
        Assertions.assertEquals(Messager.incorrectCommand("Ru"), "Неверная команда");
    }

    @Test
    @DisplayName("incorrectCommand -> Null")
    void incorrectCommandNull(){
        Assertions.assertEquals(Messager.incorrectCommand(null), "Yanlış komanda");
    }

    @Test
    @DisplayName("startMessage -> Az")
    void startMessageAz(){
        Assertions.assertEquals(Messager.startMessage("Az"), "Başlamaq üçün /start komandasını daxil edin");
    }

    @Test
    @DisplayName("startMessage -> En")
    void startMessageEn(){
        Assertions.assertEquals(Messager.startMessage("En"), "Please type /start to start");
    }

    @Test
    @DisplayName("startMessage -> Ru")
    void startMessageRu(){
        Assertions.assertEquals(Messager.startMessage("Ru"), "Пожалуйста, введите /start, чтобы начать");
    }

    @Test
    @DisplayName("startMessage -> Null")
    void startMessageNull(){
        Assertions.assertEquals(Messager.startMessage(null), "Başlamaq üçün /start komandasını daxil edin");
    }

    @Test
    @DisplayName("stopMessage -> Az")
    void stopMessageAz(){
        Assertions.assertEquals(Messager.stopMessage("Az"), "Sizin sessiyanız silindi, yenidən başlamaq üçün /start komandasını daxil edin");
    }

    @Test
    @DisplayName("stopMessage -> En")
    void stopMessageEn(){
        Assertions.assertEquals(Messager.stopMessage("En"), "Your session was removed, you can restart with typing /start");
    }

    @Test
    @DisplayName("stopMessage -> Ru")
    void stopMessageRu(){
        Assertions.assertEquals(Messager.stopMessage("Ru"), "Ваш сеанс был удален, вы можете перезапустить его, набрав /start");
    }

    @Test
    @DisplayName("stopMessage -> Null")
    void stopMessageNull(){
        Assertions.assertEquals(Messager.stopMessage(null), "Sizin sessiyanız silindi, yenidən başlamaq üçün /start komandasını daxil edin");
    }

    @Test
    @DisplayName("activeSessionMessage -> Az")
    void activeSessionMessageAz(){
        Assertions.assertEquals(Messager.activeSessionMessage("Az"), "Sizin aktiv sessiyanız var, yenidən başlamaq üçün ilk öncə /stop komandasını daxil edin");
    }

    @Test
    @DisplayName("activeSessionMessage -> En")
    void activeSessionMessageEn(){
        Assertions.assertEquals(Messager.activeSessionMessage("En"), "You have active session, please first type /stop to restart");
    }

    @Test
    @DisplayName("activeSessionMessage -> Ru")
    void activeSessionMessageRu(){
        Assertions.assertEquals(Messager.activeSessionMessage("Ru"), "У вас активный сеанс, введите /stop, чтобы перезапустить");
    }

    @Test
    @DisplayName("activeSessionMessage -> Null")
    void activeSessionMessageNull(){
        Assertions.assertEquals(Messager.activeSessionMessage(null), "Sizin aktiv sessiyanız var, yenidən başlamaq üçün ilk öncə /stop komandasını daxil edin");
    }

    @Test
    @DisplayName("sessionMessage -> Az")
    void sessionMessageAz(){
        Assertions.assertEquals(Messager.sessionMessage("Az"), "Sizin aktiv sessiyanız yoxdur, zəhmət olmasa başlamaq üçün /start komandasını daxil edin");
    }

    @Test
    @DisplayName("sessionMessage -> En")
    void sessionMessageEn(){
        Assertions.assertEquals(Messager.sessionMessage("En"), "You don't have active session, please type /start to start");
    }

    @Test
    @DisplayName("sessionMessage -> Ru")
    void sessionMessageRu(){
        Assertions.assertEquals(Messager.sessionMessage("Ru"), "У вас нет активного сеанса, введите /start, чтобы начать");
    }

    @Test
    @DisplayName("sessionMessage -> Null")
    void sessionMessageNull(){
        Assertions.assertEquals(Messager.sessionMessage(null), "Sizin aktiv sessiyanız yoxdur, zəhmət olmasa başlamaq üçün /start komandasını daxil edin");
    }

    @Test
    @DisplayName("loadOfferQuestion -> Az")
    void loadOfferQuestionAz(){
        Assertions.assertEquals(Messager.loadOfferQuestion("Az"), "Yeni təkliflər görmək istəyirsinizmi?");
    }

    @Test
    @DisplayName("loadOfferQuestion -> En")
    void loadOfferQuestionEn(){
        Assertions.assertEquals(Messager.loadOfferQuestion("En"), "Do you want to load new offers?");
    }

    @Test
    @DisplayName("loadOfferQuestion -> Ru")
    void loadOfferQuestionRu(){
        Assertions.assertEquals(Messager.loadOfferQuestion("Ru"), "Хотите загрузить новые предложения?");
    }

    @Test
    @DisplayName("loadOfferQuestion -> Null")
    void loadOfferQuestionNull(){
        Assertions.assertEquals(Messager.loadOfferQuestion(null), "Yeni təkliflər görmək istəyirsinizmi?");
    }

    @Test
    @DisplayName("loadOfferAction -> Az")
    void loadOfferActionAz(){
        Assertions.assertEquals(Messager.loadOfferAction("Az"), "Yüklə...");
    }

    @Test
    @DisplayName("loadOfferAction -> En")
    void loadOfferActionEn(){
        Assertions.assertEquals(Messager.loadOfferAction("En"), "Load...");
    }

    @Test
    @DisplayName("loadOfferAction -> Ru")
    void loadOfferActionRu(){
        Assertions.assertEquals(Messager.loadOfferAction("Ru"), "Загрузить...");
    }

    @Test
    @DisplayName("loadOfferAction -> Null")
    void loadOfferActionNull(){
        Assertions.assertEquals(Messager.loadOfferAction(null), "Yüklə...");
    }

    @Test
    @DisplayName("expireMessage -> Az")
    void expireMessageAz(){
        Assertions.assertEquals(Messager.expireMessage("Az"), "Sizin sessiyanızın müddəti bitdi, Yeni anketə başlamaq üçün /start komandasını daxil edin.");
    }

    @Test
    @DisplayName("expireMessage -> En")
    void expireMessageEn(){
        Assertions.assertEquals(Messager.expireMessage("En"), "Your session was expired, type /start to start a new survey");
    }

    @Test
    @DisplayName("expireMessage -> Ru")
    void expireMessageRu(){
        Assertions.assertEquals(Messager.expireMessage("Ru"), "Срок действия вашей сессии истек, введите /start, чтобы начать новый опрос");
    }

    @Test
    @DisplayName("expireMessage -> Null")
    void expireMessageNull(){
        Assertions.assertEquals(Messager.expireMessage(null), "Sizin sessiyanızın müddəti bitdi, Yeni anketə başlamaq üçün /start komandasını daxil edin.");
    }

}