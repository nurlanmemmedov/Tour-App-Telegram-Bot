package com.example.telegrambotapi.services;
import com.example.telegrambotapi.bot.TourBot;
import com.example.telegrambotapi.enums.ActionType;
import com.example.telegrambotapi.enums.RequestStatus;
import com.example.telegrambotapi.models.QuestionBag;
import com.example.telegrambotapi.models.entities.Offer;
import com.example.telegrambotapi.models.entities.Question;
import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.repositories.OfferRepository;
import com.example.telegrambotapi.repositories.RequestRepository;
import com.example.telegrambotapi.services.interfaces.TourService;
import com.example.telegrambotapi.services.interfaces.DataService;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.telegrambotapi.utils.Validator.*;

@Component
public class TourServiceImpl implements TourService {

    private DataService service;
    private QuestionBag questionBag;
    private RequestRepository requestRepository;
    private OfferRepository offerRepository;
    private TourBot bot;


    public TourServiceImpl(DataService service,
                           QuestionBag questionBag,
                           RequestRepository requestRepository,
                           OfferRepository offerRepository,
                           @Lazy TourBot bot){
        this.service = service;
        this.questionBag = questionBag;
        this.requestRepository = requestRepository;
        this.offerRepository = offerRepository;
        this.bot = bot;
    }

    @Override
    @SneakyThrows
    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery())
        {
            return handleCallBackQuery(update);
        }
        Message message = update.getMessage();
        if (message.getText().startsWith("/")){
            return handleCommands(message);
        }
        return handleMessage(message);
    }

    @SneakyThrows
    private BotApiMethod<?> giveQuestion(Message message, Question question){
        String chatId = message.getChatId().toString();
        service.setCurrentQuestion(message.getFrom().getId(), question);
        SendMessage sendMessage = new SendMessage(chatId, question.getQuestionText(service.getSelectedLanguage(message.getFrom().getId())));

        if (questionBag.hasButton(question)){
            sendMessage = new SendMessage(chatId, question.getQuestionText(service.getSelectedLanguage(message.getFrom().getId())))
                    .setReplyMarkup(getButtons(message, question));
        }
        if (questionBag.isLast(question)) service.endPoll(message.getFrom().getId());
        return sendMessage;
    }

    private ReplyKeyboardMarkup getButtons(Message message, Question question){
        List<KeyboardRow> keyboardButtonsRow= new ArrayList<>();
        question.getActions().stream().forEach(a -> {
            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton()
                    .setText(a.getAnswer(service.getSelectedLanguage(message.getFrom().getId())));
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
            if (service.hasActiveSession(clientId)){
                return new SendMessage(chatId, "You have active session, please type /stop to restart");
            }
            service.createSession(message);
            return giveQuestion(message, questionBag.getFirstQuestion());
        }
        else if (message.getText().equals("/stop")){
            service.stopActivePoll(clientId);
            return new SendMessage(chatId, "Your session was removed, you can restart with typing /start");
        }
        return new SendMessage(chatId, "Incorrect command");
    }

    private BotApiMethod<?> handleMessage(Message message){
        Integer clientId = message.getFrom().getId();
        String chatId = message.getChatId().toString();

        if (!service.hasActiveSession(clientId)){
            return new SendMessage(chatId, "Please type /start to start");
        }
        Question question = service.getCurrentQuestion(clientId);
        if (!validate(question, message.getText())) return new SendMessage(chatId, "Incorrect answer");
        if (questionBag.isFirst(question)) service.setSelectedLanguage(clientId, message.getText());
        service.saveUserData(clientId, question.getQuestionKey(), message.getText());
        return giveQuestion(message, questionBag.getNext(question, message));
    }

    private BotApiMethod<?> handleCallBackQuery(Update update){
        Integer clientId = update.getCallbackQuery().getFrom().getId();
        if(update.getCallbackQuery().getData().equals("load")){
            loadNextOffers(clientId);
        }
        return null;
    }

    @SneakyThrows
    private void loadNextOffers(Integer clientId) {
        List<Request> requests = requestRepository.findByClientId(clientId);
        Request activeRequest = requests.stream()
                .filter(r -> r.getStatus() == RequestStatus.ACTIVE).findFirst().orElse(null);
        if (activeRequest == null) return; //TODO
        activeRequest.setHasNext(true);
        activeRequest.getNextNotSentRequests().stream().forEach(o -> {
            sendOffer(o);
        });
        if (activeRequest.getNextNotSentRequests().size() % 5 == 0){
            activeRequest.setHasNext(false);
            List<InlineKeyboardButton> keyboardButtonsRow= new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton()
                    .setText("Load..");
            button.setCallbackData("load");
            keyboardButtonsRow.add(button);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(keyboardButtonsRow);
            inlineKeyboardMarkup.setKeyboard(rowList);
            bot.execute(new SendMessage(activeRequest.getChatId(), "Do you want to load new Messages?")
                    .setReplyMarkup(inlineKeyboardMarkup));
        }
        requestRepository.save(activeRequest);
    }

    private void sendOffer(Offer offer){
        bot.sendPhoto(offer.getRequest().getChatId(), offer.getPath());
        offer.setIsSent(true);
        offerRepository.save(offer);
    }

}
