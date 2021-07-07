package com.example.telegrambotapi.controllers;

import com.example.telegrambotapi.bot.TourBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class TourController {
    private final TourBot telegramBot;

    public TourController(TourBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostMapping
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }
}
