package com.example.telegrambotapi.bot;

import com.example.telegrambotapi.services.interfaces.TourService;

import com.pengrad.telegrambot.request.SetWebhook;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.net.URL;

public class TourBot extends TelegramWebhookBot {

    String botToken;
    String botUsername;
    TourService service;
    String webhookPath;
    SetWebhook setWebhook = new SetWebhook();

    public TourBot(TourService service){
        this.service = service;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
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

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setWebhookPath(String webhookPath) {
        this.webhookPath = webhookPath;
    }

    @SneakyThrows
    public void sendPhoto(Long chatId,  String imagePath) {
        URL url = new URL(imagePath);
        File file = new File("filename.jpg");
        FileUtils.copyURLToFile(url, file);
        SendPhoto sendPhoto = new SendPhoto().setPhoto(file);
        sendPhoto.setChatId(chatId);
        execute(sendPhoto);
    }
}
