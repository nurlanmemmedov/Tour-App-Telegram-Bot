package com.example.telegrambotapi.utils.cache;

import com.example.telegrambotapi.models.entities.Action;
import com.example.telegrambotapi.models.entities.Question;
import com.example.telegrambotapi.models.entities.Session;
import com.example.telegrambotapi.repositories.QuestionRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class DataCacheImpl implements DataCache{

    private QuestionRepository repository;

    public DataCacheImpl(QuestionRepository repository){
        this.repository = repository;
    }

    private Map<String, Map<Integer, Question>>  questions = new HashMap<>();
    private Map<String, Session> sessions = new HashMap<>();
    private Map<Integer, Session> activeSessions = new HashMap<>();

    @Override
    public void setUserActiveSession(Message message){
        Session session = Session.builder()
                .uuid(UUID.randomUUID().toString())
                .chatId(message.getChatId())
                .clientId(message.getFrom().getId()).build();
        activeSessions.put(session.getClientId(), session);
    }

    @Override
    public void removeSession(Integer clientId){
        Session session = activeSessions.get(clientId);
        sessions.put(session.getUuid(), session);
        activeSessions.remove(clientId);
    }

    @Override
    public void setUsersCurrentBotState(Integer clientId, Question question) {
        activeSessions.get(clientId).setCurrentQuestion(question);
    }

    @Override
    public Question getUsersCurrentBotState(Integer clientId) {
        if (activeSessions.get(clientId) == null) return null;
        return activeSessions.get(clientId).getCurrentQuestion();
    }

    @Override
    public void saveUserData(Integer clientId, String key, String answer){
        Map<String, String> userData = activeSessions.get(clientId).getData();
        if(userData == null){
            userData = new HashMap<>();
        }
        userData.put(key, answer);
    }

    @Override
    public Map<String, String> getUserData(Integer clientId){
        return activeSessions.get(clientId).getData();
    }

    @Override
    public void removeUserData(Integer clientId){
        activeSessions.remove(clientId);
    }

    @Override
    public void setSelectedLanguage(Integer clientId, String code){
        activeSessions.get(clientId).setUserLanguage(code);
    }

    @Override
    public String getSelectedLanguage(Integer clientId){
        return activeSessions.get(clientId).getUserLanguage();
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
