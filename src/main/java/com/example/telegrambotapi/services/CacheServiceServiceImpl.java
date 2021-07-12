package com.example.telegrambotapi.services;

import com.example.telegrambotapi.enums.RequestStatus;
import com.example.telegrambotapi.models.entities.Question;
import com.example.telegrambotapi.models.Session;
import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.repositories.QuestionRepository;
import com.example.telegrambotapi.repositories.SessionRepository;
import com.example.telegrambotapi.services.interfaces.CacheService;
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

/**
 * this class implements CacheService
 * is used to make operations with sessions in cache
 */
@Service
public class CacheServiceServiceImpl implements CacheService {

    private QuestionRepository repository;
    private RequestService requestService;
    private SessionRepository redisRepository;
    private RabbitmqService rabbitmqService;

    private Map<String, Map<Integer, Question>>  questions = new HashMap<>();

    public CacheServiceServiceImpl(QuestionRepository repository,
                                   SessionRepository redisRepository,
                                   RabbitmqService rabbitmqService,
                                   RequestService requestService){
        this.repository = repository;
        this.redisRepository = redisRepository;
        this.rabbitmqService = rabbitmqService;
        this.requestService = requestService;
    }

    /**
     * {@inheritDoc}
     * @param message
     */
    @Override
    public void createSession(Message message){
        Session session = Session.builder()
                .uuid(UUID.randomUUID().toString())
                .chatId(message.getChatId())
                .clientId(message.getFrom().getId()).build();
        redisRepository.save(session);
    }

    /**
     * {@inheritDoc}
     * @param clientId
     */
    @Override
    public void endPoll(Integer clientId){
        Session session = redisRepository.find(clientId);
        requestService.save(session);
        rabbitmqService.sendToPollQueue(session);
        redisRepository.delete(clientId);
    }

    /**
     * {@inheritDoc}
     * @param clientId
     * @param question
     */
    @Override
    public void setCurrentQuestion(Integer clientId, Question question) {
        Session session = redisRepository.find(clientId);
        session.setCurrentQuestion(question);
        redisRepository.save(session);
    }

    /**
     * {@inheritDoc}
     * @param clientId
     * @return
     */
    @Override
    public Question getCurrentQuestion(Integer clientId) {
        if (redisRepository.find(clientId) == null) return null;
        return redisRepository.find(clientId).getCurrentQuestion();
    }

    /**
     * {@inheritDoc}
     * @param clientId
     * @param key
     * @param answer
     */
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

    /**
     * {@inheritDoc}
     * @param clientId
     * @return
     */
    @Override
    public Boolean hasActiveSession(Integer clientId){
         return (redisRepository.find(clientId) != null
                 || requestService.findByClientId(clientId).stream()
                 .anyMatch(r -> r.getStatus() == RequestStatus.ACTIVE));
    }

    /**
     * {@inheritDoc}
     * @param clientId
     */
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

    /**
     * {@inheritDoc}
     * @param clientId
     * @param code
     */
    @Override
    public void setSelectedLanguage(Integer clientId, String code){
        Session session = redisRepository.find(clientId);
        session.setUserLanguage(code);
        redisRepository.save(session);
    }

    /**
     * {@inheritDoc}
     * @param clientId
     * @return
     */
    @Override
    public String getSelectedLanguage(Integer clientId){
        return redisRepository.find(clientId).getUserLanguage();
    }

    /**
     * {@inheritDoc}
     * @param id
     */
    @Override
    public void setQuestions(String id) {
        Map<Integer, Question> questionMap = new HashMap<>();
        repository.findAll().stream().forEach(q -> questionMap.put(q.getId(), q));
        questions.put(id, questionMap);
    }

    /**
     * {@inheritDoc}
     * @param id
     * @return
     */
    @Override
    public Map<Integer, Question> getQuestion(String id) {
        return questions.get(id);
    }
}
