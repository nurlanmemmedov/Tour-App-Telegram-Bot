//package com.example.telegrambotapi.configs;
//
//import com.example.telegrambotapi.bot.TourBot;
//import com.pengrad.telegrambot.model.BotCommand;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.telegram.telegrambots.bots.TelegramWebhookBot;
//
//@Configuration
//public class TourBotConfig {
//    @Bean
//    TelegramWebhookBot tourBotWebHook(@Value("${telegrambot.botToken}")String username,
//                                      @Value("touralbot")String token,
//                                      @Value("touralbot")String ngrokurl,
//                                      @Value("touralbot")String webhookpath){
//        TelegramWebhookBot bot = new TourBot(username, token, ngrokurl, webhookpath);
//        bot.setWebhook(ngrokurl);
//        BotCommand start = new BotCommand("start", "starts the conversation");
//        BotCommand stop = new BotCommand("stop", "stops the conversation");
//    }
//
//
//
//}
