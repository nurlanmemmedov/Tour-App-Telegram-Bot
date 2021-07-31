package com.example.telegrambotapi.listeners;

import com.example.telegrambotapi.configs.RabbitmqConfig;
import com.example.telegrambotapi.models.entities.Offer;
import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.repositories.OfferRepository;
import com.example.telegrambotapi.repositories.RequestRepository;
import com.example.telegrambotapi.repositories.redis.SentOfferRepository;
import com.example.telegrambotapi.services.interfaces.TourService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class OfferListener {

    private OfferRepository repository;
    private SentOfferRepository sentOfferRepository;
    private RequestRepository requestRepository;
    private TourService service;

    public OfferListener(OfferRepository repository,
                         TourService service,
                         RequestRepository requestRepository,
                         SentOfferRepository sentOfferRepository){
        this.repository = repository;
        this.sentOfferRepository = sentOfferRepository;
        this.requestRepository = requestRepository;
        this.service = service;
    }

    @RabbitListener(queues = RabbitmqConfig.OFFERQUEUE)
    public void consumeMessageFromQueue(Offer offer) throws TelegramApiException {
        Request request = requestRepository.getRequestByUuid(offer.getUuid());
        if (request == null) return; //TODO
        Offer newOffer = Offer.builder().uuid(offer.getUuid())
                .image(offer.getImage()).offerId(offer.getOfferId())
                .isSent(false).request(request).build();
        requestRepository.save(request);
        repository.save(newOffer);
        service.sendOffer(newOffer);
        return;
    }
}