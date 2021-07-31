package com.example.telegrambotapi.services;

import com.example.telegrambotapi.enums.RequestStatus;
import com.example.telegrambotapi.models.Session;
import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.repositories.RequestRepository;
import com.example.telegrambotapi.services.interfaces.RequestService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * this class implements the RequestService
 * is used to make operations on request entities
 */
@Service
public class RequestServiceImpl implements RequestService {

    private RequestRepository repository;

    public RequestServiceImpl(RequestRepository repository){
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     * @param session
     */
    @Override
    public void create(Session session) {
        Request request = Request.builder().clientId(session.getClientId())
                .uuid(session.getUuid())
                .chatId(session.getChatId())
                .isActive(true).build();
        repository.save(request);
    }

    /**
     * {@inheritDoc}
     * @param request
     * @return
     */
    @Override
    public Request save(Request request) {
        return repository.save(request);
    }

    /**
     * {@inheritDoc}
     * @param clientId
     * @return
     */
    @Override
    public List<Request> findByClientId(Integer clientId) {
        return repository.findByClientId(clientId);
    }

    /**
     * {@inheritDoc}
     * @param uuid
     * @return
     */
    @Override
    public Request getByUuid(String uuid) {
        return repository.getRequestByUuid(uuid);
    }
}
