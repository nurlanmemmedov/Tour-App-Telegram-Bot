package com.example.telegrambotapi.utils.cache;

import com.example.telegrambotapi.dtos.TourRequestDto;
import com.example.telegrambotapi.models.Action;
import com.example.telegrambotapi.models.Question;
import com.example.telegrambotapi.repositories.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataCacheImpl implements DataCache{

    private QuestionRepository repository;

    public DataCacheImpl(QuestionRepository repository){
        this.repository = repository;
    }

    private Map<String, Map<Integer, Question>>  questions = new HashMap<>();
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

    @Override
    public void setQuestions(String id) {
        Map<Integer, Question> questionMap = new HashMap<>();
        repository.findAll().stream().forEach(q -> questionMap.put(q.getId(), q));
        questions.put(id, questionMap);
    }

    @Override
    public Map<Integer, Question> getQuestion(String id) {
        return questions.get(id);
    }
}
