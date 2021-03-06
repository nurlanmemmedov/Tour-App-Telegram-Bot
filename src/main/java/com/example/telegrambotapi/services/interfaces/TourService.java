package com.example.telegrambotapi.services.interfaces;

import com.example.telegrambotapi.models.entities.Offer;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TourService {
    BotApiMethod<?> handleUpdate(Update update);
    void sendOffer(Offer offer);
}
