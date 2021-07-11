package com.example.telegrambotapi.utils.cache;

import com.example.telegrambotapi.configs.RabbitmqConfig;
import com.example.telegrambotapi.enums.RequestStatus;
import com.example.telegrambotapi.models.entities.Question;
import com.example.telegrambotapi.models.Session;
import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.repositories.QuestionRepository;
import com.example.telegrambotapi.repositories.RequestRepository;
import com.example.telegrambotapi.repositories.SessionRepository;
import com.example.telegrambotapi.services.interfaces.RabbitmqService;
import com.example.telegrambotapi.services.interfaces.RequestService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DataCacheImpl implements DataCache{

    private QuestionRepository repository;
    private RequestService requestService;
    private SessionRepository redisRepository;
    private RabbitmqService rabbitmqService;
    @Autowired
    private RabbitTemplate template;
    private Map<String, Map<Integer, Question>>  questions = new HashMap<>();

    public DataCacheImpl(QuestionRepository repository,
                         SessionRepository redisRepository,
                         RabbitmqService rabbitmqService,
                         RequestService requestService){
        this.repository = repository;
        this.redisRepository = redisRepository;
        this.rabbitmqService = rabbitmqService;
        this.requestService = requestService;
    }

    @Override
    public void setUserActiveSession(Message message){
        Session session = Session.builder()
                .uuid(UUID.randomUUID().toString())
                .chatId(message.getChatId())
                .clientId(message.getFrom().getId()).build();
        redisRepository.save(session);
    }

    @Override
    public void endPoll(Integer clientId){
        Session session = redisRepository.find(clientId);
        requestService.save(session);
        rabbitmqService.sendToPollQueue(session);
        redisRepository.delete(clientId);
    }

    @Override
    public void setUsersCurrentBotState(Integer clientId, Question question) {
        Session session = redisRepository.find(clientId);
        session.setCurrentQuestion(question);
        redisRepository.save(session);
    }

    @Override
    public Question getUsersCurrentBotState(Integer clientId) {
        if (redisRepository.find(clientId) == null) return null;
        return redisRepository.find(clientId).getCurrentQuestion();
    }

    @Override
    public void saveUserData(Integer clientId, String key, String answer){
        Session session = redisRepository.find(clientId);
        Map<String, String> userData = session.getData();
        if(userData == null){
            userData = new HashMap<>();
        }
        userData.put(key, answer);
        session.setData(userData);
        redisRepository.save(session);
    }

    @Override
    public Boolean hasActiveSession(Integer clientId){
         return (redisRepository.find(clientId) != null
                 || requestService.findByClientId(clientId).stream()
                 .anyMatch(r -> r.getStatus() == RequestStatus.ACTIVE));
    }

    @Override
    public void stopActivePoll(Integer clientId){
        redisRepository.delete(clientId);
        List<Request> requests = requestService.findByClientId(clientId);
        requests.stream().filter(r -> r.getStatus() != RequestStatus.STOPPED)
                .forEach(r -> {
                    requestService.changeStatusByClientId(r.getId(), RequestStatus.STOPPED);
                    rabbitmqService.sendToStopQueue(r.getUuid());
                });
    }

    @Override
    public void setSelectedLanguage(Integer clientId, String code){
        Session session = redisRepository.find(clientId);
        session.setUserLanguage(code);
        redisRepository.save(session);
    }

    @Override
    public String getSelectedLanguage(Integer clientId){
        return redisRepository.find(clientId).getUserLanguage();
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
