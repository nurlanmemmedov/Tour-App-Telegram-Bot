package com.example.telegrambotapi.bot;


import com.example.telegrambotapi.enums.ActionType;
import com.example.telegrambotapi.models.Action;
import com.example.telegrambotapi.models.Question;
import com.example.telegrambotapi.repositories.ActionRepository;
import com.example.telegrambotapi.repositories.QuestionRepository;
import com.example.telegrambotapi.utils.cache.DataCache;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BotServiceImpl implements BotService{

    @Autowired
    private QuestionRepository repository;
    @Autowired
    private ActionRepository actionRepository;
    private DataCache cache;

    public BotServiceImpl(DataCache cache){
        this.cache = cache;
    }

    @Override
    @SneakyThrows
    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery())
        {
            String chatId = update.getCallbackQuery().getFrom().getId().toString();
            String answer = update.getCallbackQuery().getData();
            Question question = cache.getUsersCurrentBotState(chatId);
            // LANGUAGE
            if (question.getId() == 1){
                cache.setSelectedLanguage(answer);
            }

            //TODO
            Action action = question.getActions().stream()
                    .filter(a -> a.getAnswer().equals(answer)).collect(Collectors.toList()).get(0);
            return giveQuestion(chatId, action.getNextQuestion().getId());
        }
        Message message = update.getMessage();
        if (message != null && message.getText().startsWith("/start")){
            return giveQuestion(message.getChatId().toString(), 1);
        }
        if (message != null && message.hasText()){
            System.out.println(message.getText());
            String chatId = message.getChatId().toString();
            Question question = cache.getUsersCurrentBotState(chatId);

            if (question != null){
                System.out.println(question.getActions().size());
                Action action = question.getActions().get(0);
                return giveQuestion(chatId, action.getNextQuestion().getId());
            }
        }
        return null;
    }


    @SneakyThrows
    private BotApiMethod<?> giveQuestion(String chatId, int questionId){
        Question question = repository.getById(questionId);
        cache.setUsersCurrentBotState(chatId, question);
        boolean hasButton = question.getActions().stream().anyMatch(a -> a.getType() == ActionType.BUTTON);
        if (hasButton){
            return new SendMessage(chatId, question.getQuestionText(cache.getSelectedLanguage())).setReplyMarkup(getButtons(hasButton?question:null));
        }
        return new SendMessage(chatId, question.getQuestionText(cache.getSelectedLanguage()));
    }

    private InlineKeyboardMarkup getButtons(Question question){
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        question.getActions().stream().forEach(a -> {
            InlineKeyboardButton button = new InlineKeyboardButton().setText(a.getAnswer(cache.getSelectedLanguage()));
            button.setCallbackData(a.getAnswer());
            keyboardButtonsRow1.add(button);
        });
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
