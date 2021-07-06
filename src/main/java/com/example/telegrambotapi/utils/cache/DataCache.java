package com.example.telegrambotapi.utils.cache;

import com.example.telegrambotapi.dtos.TourRequestDto;
import com.example.telegrambotapi.models.Question;

public interface DataCache {
    void setUsersCurrentBotState(int id, Question question);

    Question getUsersCurrentBotState(int id);

    TourRequestDto getUserProfileData(int id);

    void saveUserProfileData(int id, TourRequestDto tourRequestData);
}
