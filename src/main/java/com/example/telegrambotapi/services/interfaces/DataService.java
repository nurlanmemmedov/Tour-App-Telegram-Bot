package com.example.telegrambotapi.services.interfaces;

import com.example.telegrambotapi.dtos.SelectedOfferDto;
import com.example.telegrambotapi.models.entities.Question;
import com.example.telegrambotapi.models.entities.Request;
import org.joda.time.LocalDate;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

/**
 * represents the cache service throughout the application
 * is used to make operations with sessions in cache
 */
public interface DataService {

    /**
     * creates new session object
     * @param message
     */
    void createSession(Message message);

    /**
     * ends poll
     * @param clientId
     */
    void endPoll(Integer clientId);

    /**
     * completes poll
     * @param clientId
     */
    BotApiMethod<?> sendSelection(Integer clientId);

    /**
     * sets current question to user's session
     * @param clientId
     * @param question
     */
    void setCurrentQuestion(Integer clientId, Question question);

    /**
     * gets user's current question state
     * @param clientId
     * @return
     */
    Question getCurrentQuestion(Integer clientId);

    /**
     * saves answer to user's poll data
     * @param clientId
     * @param key
     * @param answer
     */
    void saveUserData(Integer clientId, String key, String answer);

    /**
     * checks if the user has any session with active status
     * @param clientIdd
     * @return
     */
    Boolean hasActiveSession(Integer clientIdd);

    /**
     * disable active poll
     * @param clientId
     */
    void disableActivePoll(Integer clientId);

    /**
     * expires active poll
     * @param request
     */
    void expireActivePoll(Request request);

    /**
     * stops the user's active poll
     * @param clientId
     */
    void stopActivePoll(Integer clientId);

    /**
     * sets the selected language to user's session
     * @param clientId
     * @param language
     */
    void setSelectedLanguage(Integer clientId, String language);

    /**
     * gets the user's selected language
     * @param clientId
     * @return
     */
    String getSelectedLanguage(Integer clientId);

    /**
     * sets selected offer data to redis
     * @param offer
     */
    void setSelectedOffer(SelectedOfferDto offer);

    /**
     * gets user's selected offer data from redis
     * @param clientId
     * @return
     */
    SelectedOfferDto getSelectedOffer(Integer clientId);

    /**
     * gets month of custom calender
     * @param clientId
     * @return
     */
    LocalDate getCalendarMonth(Integer clientId);

    /**
     * sets month of custom calender
     * @param clientId
     * @return
     */
    void setCalendarMonth(Integer clientId, LocalDate date);
}
