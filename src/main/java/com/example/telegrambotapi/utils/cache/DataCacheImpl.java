package com.example.telegrambotapi.utils.cache;

import com.example.telegrambotapi.models.entities.Question;
import com.example.telegrambotapi.models.Session;
import com.example.telegrambotapi.repositories.QuestionRepository;
import com.example.telegrambotapi.repositories.SessionRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class DataCacheImpl implements DataCache{

    private QuestionRepository repository;
    private SessionRepository redisRepository;
    private Map<String, Map<Integer, Question>>  questions = new HashMap<>();

    public DataCacheImpl(QuestionRepository repository, SessionRepository redisRepository){
        this.repository = repository;
        this.redisRepository = redisRepository;
    }

    @Override
    public void setUserActiveSession(Message message){
        Session session = Session.builder()
                .uuid(UUID.randomUUID().toString())
                .chatId(message.getChatId())
                .clientId(message.getFrom().getId()).build();
        redisRepository.saveActiveSession(session);
    }

    @Override
    public void removeSession(Integer clientId){
        Session session = redisRepository.findActiveSessionUuid(clientId);
        redisRepository.saveSession(session);
        redisRepository.deleteActiveSession(clientId);
    }

    @Override
    public void setUsersCurrentBotState(Integer clientId, Question question) {
        Session session = redisRepository.findActiveSessionUuid(clientId);
        session.setCurrentQuestion(question);
        redisRepository.saveActiveSession(session);
    }

    @Override
    public Question getUsersCurrentBotState(Integer clientId) {
        if (redisRepository.findActiveSessionUuid(clientId) == null) return null;
        return redisRepository.findActiveSessionUuid(clientId).getCurrentQuestion();
    }

    @Override
    public void saveUserData(Integer clientId, String key, String answer){
        Session session = redisRepository.findActiveSessionUuid(clientId);
        Map<String, String> userData = session.getData();
        if(userData == null){
            userData = new HashMap<>();
        }
        userData.put(key, answer);
        session.setData(userData);
        redisRepository.saveActiveSession(session);
    }

    @Override
    public Map<String, String> getUserData(Integer clientId){
        return redisRepository.findActiveSessionUuid(clientId).getData();
    }

    @Override
    public void removeUserData(Integer clientId){
        redisRepository.deleteActiveSession(clientId);
    }

    @Override
    public void setSelectedLanguage(Integer clientId, String code){
        Session session = redisRepository.findActiveSessionUuid(clientId);
        session.setUserLanguage(code);
        redisRepository.saveActiveSession(session);
    }

    @Override
    public String getSelectedLanguage(Integer clientId){
        return redisRepository.findActiveSessionUuid(clientId).getUserLanguage();
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
