package com.example.telegrambotapi.configs;


import com.example.telegrambotapi.bot.TourBot;
import com.example.telegrambotapi.services.interfaces.TourService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Setter
@Getter
@Configuration
@ConfigurationProperties("telegrambot")
public class BotConfig {

    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public TourBot StudentBot(TourService service) {

        TourBot mySuperTelegramBot = new TourBot(service);
        mySuperTelegramBot.setBotUsername(botUserName);
        mySuperTelegramBot.setBotToken(botToken);
        mySuperTelegramBot.setWebhookPath(webHookPath);

        return mySuperTelegramBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
