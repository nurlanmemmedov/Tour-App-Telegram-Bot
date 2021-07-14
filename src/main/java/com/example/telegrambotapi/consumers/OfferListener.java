//package com.example.telegrambotapi.consumers;
//
//import com.example.telegrambotapi.configs.RabbitmqConfig;
//import com.example.telegrambotapi.configs.RedisConfig;
//import com.example.telegrambotapi.models.Session;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OfferListener {
//
//    @RabbitListener(queues = RabbitmqConfig.QUEUE)
//    public void consumeMessageFromQueue(Session offer) {
//        System.out.println("Offer recieved from queue : " + offer.getUuid());
//    }
//}