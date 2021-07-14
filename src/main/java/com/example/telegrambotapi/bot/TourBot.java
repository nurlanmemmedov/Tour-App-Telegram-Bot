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
        if (update.getMessage().getText().equals("/image")){
            sendPhoto(update.getMessage().getChatId(), "AAAAAAAAAAAAAAaa", "https://firebasestorage.googleapis.com/v0/b/turboaz-e8cff.appspot.com/o/WIN_20210708_08_46_05_Pro.jpg?alt=media&token=b4fa1dd9-2769-4478-b149-241d76619b46");
            return null;
        }
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
    public void sendPhoto(Long chatId, String imageCaption, String imagePath) {
        URL url = new URL(imagePath);
        File file = new File("filename.jpg");
        FileUtils.copyURLToFile(url, file);
        SendPhoto sendPhoto = new SendPhoto().setPhoto(file);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(imageCaption);
        execute(sendPhoto);
    }

//    InputStream inputStream = new ByteArrayInputStream(array);
//    InputFile inputFile = new InputFile();
//        inputFile.setMedia(inputStream, "image");

//    bot.execute(SendPhoto.builder()
//            .chatId(botSession.getChatId())
//            .photo(inputFile).build());

}
