package com.example.telegrambotapi.utils.cache;

import com.example.telegrambotapi.models.entities.Question;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

public interface DataCache {
    void setUserActiveSession(Message message);

    void removeSession(Integer clientId);

    void setUsersCurrentBotState(Integer clientId, Question question);

    Question getUsersCurrentBotState(Integer clientId);

    void saveUserData(Integer clientId, String key, String answer);

    Map<String, String> getUserData(Integer clientIdd);

    void removeUserData(Integer clientId);

    void setSelectedLanguage(Integer clientId, String code);

    String getSelectedLanguage(Integer clientId);

    void setQuestions(String id);


    Map<Integer, Question>  getQuestion(String id);
}
