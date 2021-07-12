package com.example.telegrambotapi.services.interfaces;

import com.example.telegrambotapi.enums.RequestStatus;
import com.example.telegrambotapi.models.Session;
import com.example.telegrambotapi.models.entities.Request;

import java.util.List;

/**
 * represents the request service through out the application
 * is used to make operations on request entities
 */
public interface RequestService {

    /**
     * saves request to database by given session
     * @param session
     */
    void save(Session session);

    /**
     * finds requests by given client id
     * @param clientId
     * @return
     */
    List<Request> findByClientId(Integer clientId);

    /**
     * changes status of request by given id
     * @param id
     * @param status
     */
    void changeStatusByClientId(Integer id, RequestStatus status);
}
