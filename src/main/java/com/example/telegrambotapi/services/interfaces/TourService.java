package com.example.telegrambotapi.services.interfaces;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TourService {
    BotApiMethod<?> handleUpdate(Update update);
}
