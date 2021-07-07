package com.example.telegrambotapi.utils.cache;

import com.example.telegrambotapi.dtos.TourRequestDto;
import com.example.telegrambotapi.models.Question;

public interface DataCache {
    void setUsersCurrentBotState(String id, Question question);

    Question getUsersCurrentBotState(String id);

    TourRequestDto getUserTourRequestData(String id);

    void saveUserTourRequest(String id, TourRequestDto tourRequestData);
}
