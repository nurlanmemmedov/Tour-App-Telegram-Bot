package com.example.telegrambotapi.services;

import com.example.telegrambotapi.enums.RequestStatus;
import com.example.telegrambotapi.models.Session;
import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.repositories.RequestRepository;
import com.example.telegrambotapi.services.interfaces.RequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    private RequestRepository repository;

    public RequestServiceImpl(RequestRepository repository){
        this.repository = repository;
    }

    @Override
    public void save(Session session) {
        Request request = Request.builder().clientId(session.getClientId())
                .uuid(session.getUuid())
                .chatId(session.getChatId())
                .status(RequestStatus.ACTIVE).build();
        repository.save(request);
    }

    @Override
    public List<Request> findByClientId(Integer clientId) {
        return repository.findByClientId(clientId);
    }

    @Override
    public void changeStatusByClientId(Integer id, RequestStatus status){
        Request request = repository.getById(id);
        request.setStatus(status);
        repository.save(request);
    }
}
