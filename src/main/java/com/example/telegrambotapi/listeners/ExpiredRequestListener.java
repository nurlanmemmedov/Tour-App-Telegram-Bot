package com.example.telegrambotapi.listeners;

import com.example.telegrambotapi.bot.TourBot;
import com.example.telegrambotapi.configs.RabbitmqConfig;
import com.example.telegrambotapi.models.entities.Offer;
import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.services.DataServiceImpl;
import com.example.telegrambotapi.services.interfaces.DataService;
import com.example.telegrambotapi.services.interfaces.RequestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static com.example.telegrambotapi.utils.Messager.expireMessage;

@Component
public class ExpiredRequestListener {

    private RequestService service;
    private DataService dataService;
    private TourBot bot;
    @Value("${selection.deadline}")
    private Integer deadline;

    public ExpiredRequestListener(RequestService service, DataService dataService,@Lazy TourBot bot){
        this.service = service;
        this.dataService = dataService;
        this.bot = bot;
    }

    @Transactional
    @RabbitListener(queues = RabbitmqConfig.EXPIREDQUEUE)
    public void expiredRequestListener(String uuid) {
        try {
            Request request = service.getByUuid(uuid);
            if (request.getIsActive()){
                if (request.getOffers().size() > 0){
                    request.setExpireDate(LocalDateTime.now().plusHours(deadline));
                    service.save(request);
                    return;
                }
                dataService.expireActivePoll(request);
            }
        }catch (Exception e){
        }

    }
}
