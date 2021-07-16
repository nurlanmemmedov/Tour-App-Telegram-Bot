package com.example.telegrambotapi.services.interfaces;

import com.example.telegrambotapi.dtos.SelectedOfferDto;
import com.example.telegrambotapi.models.Session;
import com.example.telegrambotapi.models.entities.Offer;

/**
 * this interface is used to interact with rabbitmq
 */
public interface RabbitmqService {
    /**
     * pushes Session object to poll queue in rabbitmq
     * @param session
     */
    void sendToPollQueue(Session session);

    /**
     * pushes uuid to stop queue in rabbitmq
     * @param uuid
     */
    void sendToStopQueue(String uuid);

    /**
     * pushes selected offer to selection queue in rabbitmq
     * @param offer
     */
    void sendToSelectionQueue(SelectedOfferDto offer);
}
