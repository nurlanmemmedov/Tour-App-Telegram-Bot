package com.example.telegrambotapi.services;

import com.example.telegrambotapi.configs.RabbitmqConfig;
import com.example.telegrambotapi.models.Session;
import com.example.telegrambotapi.services.interfaces.RabbitmqService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * this class implements RabbitmqService and used for interact with rabbitmq
 */
@Service
public class RabbitmqServiceImpl implements RabbitmqService {
    @Autowired
    private RabbitTemplate template;

    /**
     * {@inheritDoc}
     * @param session
     */
    @Override
    public void sendToPollQueue(Session session){
        template.convertAndSend(RabbitmqConfig.QUEUE, session);
    }

    /**
     * {@inheritDoc}
     * @param uuid
     */
    @Override
    public void sendToStopQueue(String uuid){
        template.convertAndSend(RabbitmqConfig.STOPQUEUE, uuid);
    }
}
