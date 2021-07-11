package com.example.telegrambotapi.services.interfaces;

import com.example.telegrambotapi.enums.RequestStatus;
import com.example.telegrambotapi.models.Session;
import com.example.telegrambotapi.models.entities.Request;

import java.util.List;

public interface RequestService {
    void save(Session session);
    List<Request> findByClientId(Integer clientId);
    void changeStatusByClientId(Integer id, RequestStatus status);
}
