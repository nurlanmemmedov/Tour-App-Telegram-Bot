package com.example.telegrambotapi.bot;

//import com.mycode.telegramstudentbot.bot.botFacade.TelegramFacade;

import com.example.telegrambotapi.services.BotService;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TourBot extends TelegramWebhookBot {

    String botToken;
    String botUsername;
    BotService service;

    public TourBot(BotService service){
        this.service = service;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        final BotApiMethod<?> replyMessageToUser = service.handleUpdate(update);
        return replyMessageToUser;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotPath() {
        return getBotPath();
    }
}
