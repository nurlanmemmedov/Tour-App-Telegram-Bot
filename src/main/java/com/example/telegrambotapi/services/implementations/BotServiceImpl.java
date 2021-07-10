package com.example.telegrambotapi.services.implementations;
import com.example.telegrambotapi.enums.ActionType;
import com.example.telegrambotapi.models.entities.Action;
import com.example.telegrambotapi.models.entities.Question;
import com.example.telegrambotapi.repositories.QuestionRepository;
import com.example.telegrambotapi.services.BotService;
import com.example.telegrambotapi.utils.cache.DataCache;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.telegrambotapi.utils.Validator.*;

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
        Message message = update.getMessage();
        if (message == null) return null;
        if (message.getText().startsWith("/")){
            return handleCommands(message);
        }
        return handleTextMessage(message);
    }

    @SneakyThrows
    private BotApiMethod<?> giveQuestion(Message message, int questionId){
        String chatId = message.getChatId().toString();
        Map<Integer, Question> questionMap = cache.getQuestion(chatId);
        Question question = questionMap.get(questionId);
        cache.setUsersCurrentBotState(message.getFrom().getId(), question);
        SendMessage sendMessage = new SendMessage(chatId, question.getQuestionText(cache.getSelectedLanguage(message.getFrom().getId())));

        boolean hasButton = question.getActions().stream()
                .anyMatch(a -> a.getType() == ActionType.BUTTON);
        if (hasButton){
            sendMessage = new SendMessage(chatId, question.getQuestionText(cache.getSelectedLanguage(message.getFrom().getId())))
                    .setReplyMarkup(getButtons(message, hasButton?question:null));
        }
        if (question.getQuestionKey().equals("last")) cache.removeSession(message.getFrom().getId());
        return sendMessage;
    }

    private ReplyKeyboardMarkup getButtons(Message message, Question question){
        List<KeyboardRow> keyboardButtonsRow= new ArrayList<>();
        question.getActions().stream().forEach(a -> {
            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton()
                    .setText(a.getAnswer(cache.getSelectedLanguage(message.getFrom().getId())));
            row.add(button);
            keyboardButtonsRow.add(row);
        });
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboardButtonsRow);
        return replyKeyboardMarkup;
    }

    private BotApiMethod<?> handleCommands(Message message){
        String chatId = message.getChatId().toString();
        Integer clientId = message.getFrom().getId();
        if (message.getText().equals("/start")){
            cache.setUserActiveSession(message);
            if (cache.getUserData(clientId) != null){
                return new SendMessage(chatId, "You have active session, please type /stop to restart");
            }
            cache.setQuestions(chatId);
            return giveQuestion(message, 1);
        }
        else if (message.getText().equals("/stop")){
            cache.removeUserData(clientId);
            return new SendMessage(chatId, "Your session was removed, you can restart with typing /start");
        }
        return new SendMessage(chatId, "Incorrect command");
    }

    private BotApiMethod<?> handleTextMessage(Message message){
        String chatId = message.getChatId().toString();
        Integer clientId = message.getFrom().getId();
        Question question = cache.getUsersCurrentBotState(clientId);
        if (!validate(question, message.getText())) return new SendMessage(chatId, "Incorrect answer");
        if (question.getQuestionKey().equals("language")){
            cache.setSelectedLanguage(clientId, message.getText());
        }
        cache.saveUserData(clientId, question.getQuestionKey(), message.getText());
        Action action = question.getActions().stream().findFirst().orElse(null);//TODO
        return giveQuestion(message, action.getNextQuestion().getId());
    }
}
