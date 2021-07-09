package com.example.telegrambotapi.utils.cache;

import com.example.telegrambotapi.dtos.TourRequestDto;
import com.example.telegrambotapi.models.Question;

import java.util.List;
import java.util.Map;

public interface DataCache {
    void setUsersCurrentBotState(String id, Question question);

    Question getUsersCurrentBotState(String id);

    void saveUserData(String id, Integer questionId, String answer);

    Map<Integer, String> getUserData(String id);

    void removeUserData(String id);

    void setSelectedLanguage(String id, String code);

    String getSelectedLanguage(String id);

    void setQuestions(String id);


    Map<Integer, Question>  getQuestion(String id);
}
