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
    private Map<Question, Map<Integer, Action>> userAnswerStates = new HashMap<>();
    private Map<String, TourRequestDto> userTourRequests = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(String id, Question question) {
        usersBotStates.put(id, question);
    }

    @Override
    public Question getUsersCurrentBotState(String id) {
        return usersBotStates.get(id);
    }

    @Override
    public TourRequestDto getUserTourRequestData(String id) {
        return userTourRequests.get(id);
    }

    @Override
    public void saveUserTourRequest(String id, TourRequestDto tourRequestData) {
        userTourRequests.put(id, tourRequestData);
    }
}
