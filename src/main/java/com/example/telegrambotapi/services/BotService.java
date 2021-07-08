package com.example.telegrambotapi.services;

import com.example.telegrambotapi.models.Question;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface BotService {
    BotApiMethod<?> handleUpdate(Update update);
}
