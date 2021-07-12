package com.example.telegrambotapi.services.interfaces;

import com.example.telegrambotapi.models.entities.Question;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

/**
 * represents the cache service throughout the application
 * is used to make operations with sessions in cache
 */
public interface CacheService {

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
     * stops the user's active poll
     * @param clientId
     */
    void stopActivePoll(Integer clientId);

    /**
     * sets the selected language to user's session
     * @param clientId
     * @param code
     */
    void setSelectedLanguage(Integer clientId, String code);

    /**
     * gets the user's selected language
     * @param clientId
     * @return
     */
    String getSelectedLanguage(Integer clientId);

    /**
     * set questions to the question map from database
     * @param id
     */
    void setQuestions(String id);

    /**
     * gets question from questions map
     * @param id
     * @return
     */
    Map<Integer, Question>  getQuestion(String id);
}
