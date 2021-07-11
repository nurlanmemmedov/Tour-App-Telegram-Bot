package com.example.telegrambotapi.services.interfaces;

import com.example.telegrambotapi.models.Session;

public interface RabbitmqService {
    void sendToPollQueue(Session session);
    void sendToStopQueue(String uuid);
}
