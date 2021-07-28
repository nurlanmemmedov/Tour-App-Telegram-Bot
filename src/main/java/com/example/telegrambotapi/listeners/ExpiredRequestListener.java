package com.example.telegrambotapi.listeners;

import com.example.telegrambotapi.configs.RabbitmqConfig;
import com.example.telegrambotapi.models.entities.Offer;
import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.services.DataServiceImpl;
import com.example.telegrambotapi.services.interfaces.DataService;
import com.example.telegrambotapi.services.interfaces.RequestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ExpiredRequestListener {

    private RequestService service;
    private DataService dataService;

    public ExpiredRequestListener(RequestService service, DataService dataService){
        this.service = service;
        this.dataService = dataService;
    }

    @Transactional
    @RabbitListener(queues = RabbitmqConfig.EXPIREDQUEUE)
    public void expiredRequestListener(String uuid) throws TelegramApiException {
        try {
            System.out.println("RABBIT");
            Request request = service.getByUuid(uuid);
            dataService.disableActivePoll(request.getClientId());
        }catch (Exception e){
        }

    }
}
