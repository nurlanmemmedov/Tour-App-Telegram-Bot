package com.example.telegrambotapi.utils;

import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.repositories.RequestRepository;
import com.example.telegrambotapi.services.interfaces.DataService;
import com.example.telegrambotapi.services.interfaces.RequestService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Scheduler {

    private RequestRepository repository;
    private DataService service;

    public Scheduler(RequestRepository repository, DataService service){
        this.repository = repository;
        this.service = service;
    }

    @Scheduled(fixedRateString =  "${expiration.check.millisecond}")
    public void expireRequests() {
        try {
            List<Request> requests = repository.getAllExpiredRequests();
            repository.updateExpiredRequests();
            requests.stream().forEach(r -> service.expireActivePoll(r));
        }catch (Exception e){
        }
    }
}