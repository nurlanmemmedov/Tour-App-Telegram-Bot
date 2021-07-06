package com.example.telegrambotapi.utils.cache;

import com.example.telegrambotapi.dtos.TourRequestDto;
import com.example.telegrambotapi.models.Question;

import java.util.HashMap;
import java.util.Map;

public class DataCacheImpl implements DataCache{

    private Map<Integer, Question> usersBotStates = new HashMap<>();
    private Map<Integer, TourRequestDto> userTourRequests = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(int id, Question question) {
        usersBotStates.put(id, question);
    }

    @Override
    public Question getUsersCurrentBotState(int id) {
        return usersBotStates.get(id);
    }

    @Override
    public TourRequestDto getUserTourRequestData(int id) {
        return userTourRequests.get(id);
    }

    @Override
    public void saveUserTourRequest(int id, TourRequestDto tourRequestData) {
        userTourRequests.put(id, tourRequestData);
    }
}
