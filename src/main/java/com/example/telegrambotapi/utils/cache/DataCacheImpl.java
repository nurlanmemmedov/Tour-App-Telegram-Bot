package com.example.telegrambotapi.utils.cache;

import com.example.telegrambotapi.dtos.TourRequestDto;
import com.example.telegrambotapi.models.Action;
import com.example.telegrambotapi.models.Question;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DataCacheImpl implements DataCache{

    private Map<String, Question> usersBotStates = new HashMap<>();
    private Map<String, Map<Integer, String>> userData = new HashMap<>();
    private Map<String, String> userLanguage = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(String id, Question question) {
        usersBotStates.put(id, question);
    }

    @Override
    public Question getUsersCurrentBotState(String id) {
        return usersBotStates.get(id);
    }

    @Override
    public void saveUserData(String id, Integer questionId, String answer){
        if (userData.get(id) == null){
            Map<Integer, String> answerMap = new HashMap<>();
            answerMap.put(questionId, answer);
            userData.put(id, answerMap);
        }
        Map<Integer, String> answerMap  = userData.get(id);
        answerMap.put(questionId, answer);

    }

    @Override
    public Map<Integer, String> getUserData(String id){
        return userData.get(id);
    }


    @Override
    public void setSelectedLanguage(String id, String code){
        userLanguage.put(id, code);
    }

    @Override
    public String getSelectedLanguage(String id){
        return userLanguage.get(id);
    }
}
