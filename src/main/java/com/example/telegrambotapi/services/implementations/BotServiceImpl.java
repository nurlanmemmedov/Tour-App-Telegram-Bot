package com.example.telegrambotapi.services.implementations;
import com.example.telegrambotapi.enums.ActionType;
import com.example.telegrambotapi.models.Action;
import com.example.telegrambotapi.models.Question;
import com.example.telegrambotapi.repositories.QuestionRepository;
import com.example.telegrambotapi.services.BotService;
import com.example.telegrambotapi.utils.cache.DataCache;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BotServiceImpl implements BotService {

    private QuestionRepository repository;
    private DataCache cache;

    public BotServiceImpl(DataCache cache, QuestionRepository repository){
        this.cache = cache;
        this.repository = repository;
    }

    @Override
    @SneakyThrows
    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) return handleCallbackQuery(update.getCallbackQuery());
        Message message = update.getMessage();
        if (message == null) return null;
        if (message.getText().startsWith("/")){
            return handleCommands(message);
        }
        return handleTextMessage(message);
    }

    @SneakyThrows
    private BotApiMethod<?> giveQuestion(String chatId, int questionId){
        Map<Integer, Question> questionMap = cache.getQuestion(chatId);
        Question question = questionMap.get(questionId);
        cache.setUsersCurrentBotState(chatId, question);
        System.out.println(question.getActions().size());
        boolean hasButton = question.getActions().stream()
                .anyMatch(a -> a.getType() == ActionType.BUTTON);
        if (hasButton){
            return new SendMessage(chatId, question.getQuestionText(cache.getSelectedLanguage(chatId)))
                    .setReplyMarkup(getButtons(chatId, hasButton?question:null));
        }
        return new SendMessage(chatId, question.getQuestionText(cache.getSelectedLanguage(chatId)));
    }

    private InlineKeyboardMarkup getButtons(String chatId, Question question){
        List<InlineKeyboardButton> keyboardButtonsRow= new ArrayList<>();
        question.getActions().stream().forEach(a -> {
            InlineKeyboardButton button = new InlineKeyboardButton().setText(a.getAnswer(cache.getSelectedLanguage(chatId)));
            button.setCallbackData(a.getAnswer());
            keyboardButtonsRow.add(button);
        });
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private BotApiMethod<?> handleCommands(Message message){
        String chatId = message.getChatId().toString();
        if (message.getText().equals("/start")){
            if (cache.getUserData(chatId) != null){
                return new SendMessage(chatId, "You have active session, please type /stop to restart");
            }
            cache.setQuestions(chatId);
            return giveQuestion(chatId, 1);
        }
        else if (message.getText().equals("/stop")){
            cache.removeUserData(chatId);
            System.out.println(cache.getUserData(chatId));
            System.out.println(cache.getUserData(chatId) == null);
            return new SendMessage(chatId, "Your session was removed, you can restart with typing /start");
        }
        return new SendMessage(chatId, "Incorrect command");
    }

    private BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery){
        String chatId = callbackQuery.getFrom().getId().toString();
        String answer = callbackQuery.getData();
        Question question = cache.getUsersCurrentBotState(chatId);

        cache.saveUserData(chatId, question.getId(),answer);

        // LANGUAGE
        if (question.getId() == 1){
            cache.setSelectedLanguage(chatId, answer);
        }

        //TODO
        Action action = question.getActions().stream()
                .filter(a -> a.getAnswer().equals(answer)).findFirst().get();
        return giveQuestion(chatId, action.getNextQuestion().getId());
    }

    private BotApiMethod<?> handleTextMessage(Message message){
        String chatId = message.getChatId().toString();
        Question question = cache.getUsersCurrentBotState(chatId);
        if (question == null) return null;
        cache.saveUserData(chatId, question.getId(), message.getText());
        System.out.println(cache.getUserData(chatId));
        Action action = question.getActions().stream().findFirst().orElse(null);//TODO
        return giveQuestion(chatId, action.getNextQuestion().getId());
    }
}
