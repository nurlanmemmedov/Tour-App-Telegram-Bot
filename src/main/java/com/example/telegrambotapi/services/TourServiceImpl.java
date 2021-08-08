package com.example.telegrambotapi.services;
import com.example.telegrambotapi.bot.TourBot;
import com.example.telegrambotapi.dtos.SelectedOfferDto;
import com.example.telegrambotapi.models.QuestionBag;
import com.example.telegrambotapi.models.entities.Offer;
import com.example.telegrambotapi.models.entities.Question;
import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.repositories.OfferRepository;
import com.example.telegrambotapi.repositories.RequestRepository;
import com.example.telegrambotapi.repositories.redis.SentOfferRepository;
import com.example.telegrambotapi.services.interfaces.TourService;
import com.example.telegrambotapi.services.interfaces.DataService;
import com.example.telegrambotapi.utils.Translator;
import lombok.SneakyThrows;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.telegrambotapi.utils.Calendar.generateKeyboard;
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
        if (!validateUpdate(update)) return null;
        Message message = update.getMessage();
        if (update.hasCallbackQuery()){
            return handleCallBackQuery(update);
        }
        if (message.getText().startsWith("/")){
            return handleCommands(message);
        }
        if (message.isReply()){
            return handleReplyMessage(message);
        }
        return handleMessage(message);
    }

    @SneakyThrows
    private BotApiMethod<?> giveQuestion(String chatId, Integer clientId,Question question, String extraMessage){
        service.setCurrentQuestion(clientId, question);
        String questionText = String.format(Translator.getQuestion(question, service.getSelectedLanguage(clientId)));
        if (extraMessage != null) questionText = extraMessage + " "+ questionText;
        SendMessage sendMessage = new SendMessage(chatId, questionText);
        if (questionBag.isDate(question))sendMessage.setReplyMarkup(generateKeyboard(LocalDate.now()));
        if (questionBag.hasButton(question))sendMessage.setReplyMarkup(getButtons(clientId, question));
        if (questionBag.isLast(question)) service.endPoll(clientId);
        if (questionBag.isEnding(question)) return service.sendSelection(clientId);
        return sendMessage;
    }

    private ReplyKeyboardMarkup getButtons(Integer clientId, Question question){
        List<KeyboardRow> keyboardButtonsRow= new ArrayList<>();
        question.getActions().stream().sorted(Comparator.comparing(a -> a.getId())).forEach(a -> {
            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton()
                    .setText(Translator.getAction(a, service.getSelectedLanguage(clientId)));
            row.add(button);
            keyboardButtonsRow.add(row);
        });
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboardButtonsRow);
        return replyKeyboardMarkup;
    }

    private BotApiMethod<?> handleCommands(Message message){
        String chatId = message.getChatId().toString();
        Integer clientId = message.getFrom().getId();
        String lang = service.getSelectedLanguage(clientId);

        if (message.getText().equals("/start")){
            if (service.hasActiveSession(clientId)){
                return new SendMessage(chatId, activeSessionMessage(lang));
            }
            try{
                System.out.println(message.getFrom().getFirstName()+message.getFrom().getLastName());
            }catch (Exception e){

            }
            service.createSession(message);
            return giveQuestion(chatId, clientId, questionBag.getFirstQuestion(), null);
        }
        else if (message.getText().equals("/stop")){
            if (!service.hasActiveSession(clientId)){
                return new SendMessage(chatId, sessionMessage(lang));
            }
            service.stopActivePoll(clientId);
            return new SendMessage(chatId, stopMessage(lang));
        }
        return new SendMessage(chatId, incorrectCommand(service.getSelectedLanguage(clientId)));
    }

    private BotApiMethod<?> handleMessage(Message message){
        Integer clientId = message.getFrom().getId();
        String chatId = message.getChatId().toString();
        String answer = message.getText();

        Question question = service.getCurrentQuestion(clientId);
        if (question == null || question.getQuestionKey().equals("last")) return null;
        if (questionBag.isDate(question)) return new SendMessage(chatId, incorrectAnswer(service.getSelectedLanguage(clientId)));
        if (!validateQuestion(question, answer)){
            return giveQuestion(chatId, clientId, question, incorrectAnswer(service.getSelectedLanguage(clientId)));
        }
        if (questionBag.isFirst(question)) service.setSelectedLanguage(clientId, answer);
        service.saveUserData(clientId, question.getQuestionKey(), answer);
        return giveQuestion(chatId, clientId, questionBag.getNext(question, answer), null);    }

    @SneakyThrows
    private BotApiMethod<?> handleCallBackQuery(Update update){
        Integer clientId = update.getCallbackQuery().getFrom().getId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        String answer = update.getCallbackQuery().getData();
        if(answer.equals("load")){
            loadNextOffers(clientId);
            return null;
        }
        else if (answer.equals("<") || answer.equals(">")){
            handleCalendar(messageId, update.getCallbackQuery().getInlineMessageId(), chatId, clientId, answer);
            return null;
        }
        Question question = service.getCurrentQuestion(clientId);
        if (question == null || question.getQuestionKey().equals("last")) return null;
        if (!validateQuestion(question, answer)){
            return new AnswerCallbackQuery().setShowAlert(true).setText(incorrectAnswer(null))
                    .setCallbackQueryId(update.getCallbackQuery().getId());}
        if (questionBag.isDate(question)){
            try {
                bot.execute(new DeleteMessage(chatId, messageId));
            }catch (TelegramApiRequestException e){
            }
            bot.execute(new SendMessage(chatId, Translator.getQuestion(question, service.getSelectedLanguage(clientId))));
            bot.execute(new SendMessage(chatId, answer));
        }
        service.saveUserData(clientId, question.getQuestionKey(), answer);
        return giveQuestion(chatId, clientId, questionBag.getNext(question, answer), null);
    }

    @SneakyThrows
    private void handleCalendar(Integer messageId, String inlineId, String chatId,Integer clientId, String answer){
        Question question = service.getCurrentQuestion(clientId);
        LocalDate currentDate = service.getCalendarMonth(clientId);
        if (!(questionBag.isDate(question))) return;
        if (answer.equals(">")){
            currentDate = currentDate.plusMonths(1);
        }else if(answer.equals("<")){
            currentDate =currentDate.minusMonths(1);
        }
        try{
            bot.execute(new EditMessageReplyMarkup().setChatId(chatId).setMessageId(messageId)
                    .setInlineMessageId(inlineId).setReplyMarkup(generateKeyboard(currentDate)));
        }catch (TelegramApiRequestException e){
        }
        service.setCalendarMonth(clientId, currentDate);
    }

    private BotApiMethod<?> handleReplyMessage(Message message){
        if (message.getText().equals("yes") || message.getText().equals("Yes") ||  message.getText().equals("Ok")
            ||  message.getText().equals("Okay")){
            Integer clientId = message.getFrom().getId();
            String chatId = message.getChatId().toString();
            Boolean hasAnySelection = service.getSelectedOffer(clientId) != null;
            Offer offer = offerRepository.getByMessageId(message.getReplyToMessage().getMessageId());
            SelectedOfferDto selectedOffer = SelectedOfferDto.builder().clientId(clientId)
                            .name(message.getFrom().getFirstName()).surname(message.getFrom()
                            .getLastName()).username(message.getFrom().getUserName())
                            .uuid(offer.getUuid()).offerId(offer.getOfferId()).build();
            service.setSelectedOffer(selectedOffer);
            if (hasAnySelection)return service.sendSelection(clientId);
            return giveQuestion(chatId, clientId, questionBag.getPhoneQuestion(), null);
        }
        return null;
    }

    @SneakyThrows
    private void loadNextOffers(Integer clientId) {
        List<Request> requests = requestRepository.findByClientId(clientId);
        Request activeRequest = requests.stream()
                .filter(r -> r.getIsActive()).findFirst().orElse(null);
        System.out.println(activeRequest.getId());
        if (activeRequest == null){
            System.out.println("AASFFFFFFFFFFFFFFF");
            return; //TODO
        }
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

    @SneakyThrows
    public Boolean validateUpdate(Update update){
        Integer clientId = null;
        if (update.hasCallbackQuery()){
            clientId = update.getCallbackQuery().getFrom().getId();
        }else if (update.hasMessage()){
            if (!update.getMessage().hasText())return false;
            if (update.getMessage().getText().startsWith("/")) return true;
            clientId = update.getMessage().getFrom().getId();
        }else {
            return false;
        }
        if (!service.hasActiveSession(clientId)){
            bot.execute(new SendMessage(clientId.toString(),sessionMessage(service.getSelectedLanguage(clientId))));
            return false;
        }
        return true;
    }

}
