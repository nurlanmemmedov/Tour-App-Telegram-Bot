package com.example.telegrambotapi.services;
import com.example.telegrambotapi.bot.TourBot;
import com.example.telegrambotapi.dtos.SelectedOfferDto;
import com.example.telegrambotapi.enums.RequestStatus;
import com.example.telegrambotapi.models.QuestionBag;
import com.example.telegrambotapi.models.entities.Offer;
import com.example.telegrambotapi.models.entities.Question;
import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.repositories.OfferRepository;
import com.example.telegrambotapi.repositories.RequestRepository;
import com.example.telegrambotapi.repositories.redis.SentOfferRepository;
import com.example.telegrambotapi.services.interfaces.RabbitmqService;
import com.example.telegrambotapi.services.interfaces.TourService;
import com.example.telegrambotapi.services.interfaces.DataService;
import com.example.telegrambotapi.utils.Messager;
import com.example.telegrambotapi.utils.Translator;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.telegrambotapi.utils.Messager.*;
import static com.example.telegrambotapi.utils.Validator.*;

@Component
public class TourServiceImpl implements TourService {

    private DataService service;
    private QuestionBag questionBag;
    private RequestRepository requestRepository;
    private OfferRepository offerRepository;
    private SentOfferRepository sentOfferRepository;
    private TourBot bot;


    public TourServiceImpl(DataService service,
                           QuestionBag questionBag,
                           RequestRepository requestRepository,
                           OfferRepository offerRepository,
                           SentOfferRepository sentOfferRepository,
                           @Lazy TourBot bot){
        this.service = service;
        this.questionBag = questionBag;
        this.requestRepository = requestRepository;
        this.offerRepository = offerRepository;
        this.sentOfferRepository = sentOfferRepository;
        this.bot = bot;
    }

    @Override
    @SneakyThrows
    public BotApiMethod<?> handleUpdate(Update update) {
        if (!validateMessage(update.getMessage()) && !update.hasCallbackQuery()) return null;
        if (update.hasCallbackQuery())
        {
            return handleCallBackQuery(update);
        }
        Message message = update.getMessage();
        if (message.getText().startsWith("/")){
            return handleCommands(message);
        }
        if (!service.hasActiveSession(update.getMessage().getFrom().getId())){
            return new SendMessage(update.getMessage().getChatId(),
                    sessionMessage(service.getSelectedLanguage(update.getMessage().getFrom().getId())));
        }
        if (message.isReply()){
            return handleReplyMessage(message);
        }

        return handleMessage(message);
    }

    @SneakyThrows
    private BotApiMethod<?> giveQuestion(Message message, Question question){
        String chatId = message.getChatId().toString();
        service.setCurrentQuestion(message.getFrom().getId(), question);
        SendMessage sendMessage = new SendMessage(chatId,
                Translator.getQuestion(question, service.getSelectedLanguage(message.getFrom().getId())));
        if (questionBag.hasButton(question)){
            sendMessage = new SendMessage(chatId,
                    Translator.getQuestion(question, service.getSelectedLanguage(message.getFrom().getId())))
                    .setReplyMarkup(getButtons(message, question));
        }
        if (questionBag.isLast(question)) service.endPoll(message.getFrom().getId());
        if (questionBag.isEnding(question)) return service.sendSelection(message.getFrom().getId());
        return sendMessage;
    }

    private ReplyKeyboardMarkup getButtons(Message message, Question question){
        List<KeyboardRow> keyboardButtonsRow= new ArrayList<>();
        question.getActions().stream().forEach(a -> {
            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton()
                    .setText(Translator.getAction(a, service.getSelectedLanguage(message.getFrom().getId())));
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
                return new SendMessage(chatId, activeSessionMessage(service.getSelectedLanguage(clientId)));
            }
            service.createSession(message);
            return giveQuestion(message, questionBag.getFirstQuestion());
        }
        else if (message.getText().equals("/stop")){
            String lang = service.getSelectedLanguage(clientId);
            service.stopActivePoll(clientId);
            return new SendMessage(chatId, Messager.stopMessage(lang));
        }
        return new SendMessage(chatId, incorrectCommand(service.getSelectedLanguage(clientId)));
    }

    private BotApiMethod<?> handleMessage(Message message){
        Integer clientId = message.getFrom().getId();
        String chatId = message.getChatId().toString();

        if (!service.hasActiveSession(clientId)){
            return new SendMessage(chatId, startMessage(service.getSelectedLanguage(clientId)));
        }
        Question question = service.getCurrentQuestion(clientId);
        if (question == null || question.getQuestionKey().equals("last")) return null;
        if (!validateQuestion(question, message.getText())){
            return new SendMessage(chatId, incorrectAnswer(service.getSelectedLanguage(clientId))
            + "  " + Translator.getQuestion(question, service.getSelectedLanguage(clientId)));
        }
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

    private BotApiMethod<?> handleReplyMessage(Message message){
        if (message.getText().equals("yes") || message.getText().equals("Yes") ||  message.getText().equals("Ok")
            ||  message.getText().equals("Okay")){
            Integer clientId = message.getFrom().getId();
            Boolean hasAnySelection = service.getSelectedOffer(clientId) != null;
            System.out.println(hasAnySelection);
            Offer offer = offerRepository.getByMessageId
                    (message.getReplyToMessage().getMessageId());
            SelectedOfferDto selectedOffer = SelectedOfferDto.builder()
                    .clientId(clientId).name(message.getFrom().getFirstName())
                    .surname(message.getFrom().getLastName()).username(message.getFrom().getUserName())
                    .uuid(offer.getUuid()).offerId(offer.getOfferId()).build();
            service.setSelectedOffer(selectedOffer);
            if (hasAnySelection){
                return service.sendSelection(clientId);
            }
            return giveQuestion(message, questionBag.getPhoneQuestion());
        }
        return null;
    }

    @SneakyThrows
    private void loadNextOffers(Integer clientId) {
        List<Request> requests = requestRepository.findByClientId(clientId);
        Request activeRequest = requests.stream()
                .filter(r -> r.getIsActive()).findFirst().orElse(null);
        if (activeRequest == null) return; //TODO
        sentOfferRepository.delete(activeRequest.getId());
        activeRequest.getNextNotSentRequests().stream().forEach(o -> {
            sendOffer(o);
        });
        requestRepository.save(activeRequest);
    }


    @Override
    @SneakyThrows
    public void sendOffer(Offer offer){
        if (sentOfferRepository.find(offer.getRequest().getId()) == 5){
            List<InlineKeyboardButton> keyboardButtonsRow= new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton()
                    .setText(loadOfferAction(service.getSelectedLanguage(offer.getRequest().getClientId())));
            button.setCallbackData("load");
            keyboardButtonsRow.add(button);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(keyboardButtonsRow);
            inlineKeyboardMarkup.setKeyboard(rowList);
            bot.execute(new SendMessage(offer.getRequest().getChatId(), loadOfferQuestion(service
                    .getSelectedLanguage(offer.getRequest().getClientId())))
                    .setReplyMarkup(inlineKeyboardMarkup));
        }
        else if(sentOfferRepository.find(offer.getRequest().getId()) < 5){
            Message message = bot.sendPhoto(offer.getRequest().getChatId(), offer.getImage(), service.getSelectedLanguage(offer.getRequest().getClientId()));
            offer.setMessageId(message.getMessageId());
            offer.setIsSent(true);
            offerRepository.save(offer);
        }
        sentOfferRepository.save(offer.getRequest().getId());
    }

}
