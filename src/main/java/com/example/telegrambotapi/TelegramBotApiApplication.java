package com.example.telegrambotapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableScheduling
public class TelegramBotApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApiApplication.class, args);
    }
}
