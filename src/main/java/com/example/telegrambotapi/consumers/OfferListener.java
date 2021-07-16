package com.example.telegrambotapi.consumers;

import com.example.telegrambotapi.bot.TourBot;
import com.example.telegrambotapi.configs.RabbitmqConfig;
import com.example.telegrambotapi.models.entities.Offer;
import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.repositories.OfferRepository;
import com.example.telegrambotapi.repositories.RequestRepository;
import com.example.telegrambotapi.repositories.redis.SentOfferRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OfferListener {

    private OfferRepository repository;
    private SentOfferRepository sentOfferRepository;
    private RequestRepository requestRepository;
    private TourBot bot;

    public OfferListener(OfferRepository repository, TourBot bot,
                         RequestRepository requestRepository,
                         SentOfferRepository sentOfferRepository){
        this.repository = repository;
        this.sentOfferRepository = sentOfferRepository;
        this.requestRepository = requestRepository;
        this.bot = bot;
    }

    @RabbitListener(queues = RabbitmqConfig.OFFERQUEUE)
    public void consumeMessageFromQueue(Offer offer) throws TelegramApiException {
        Request request = requestRepository.getRequestByUuid(offer.getUuid());
        if (request == null) return; //TODO
        Offer newOffer = Offer.builder().uuid(offer.getUuid())
                .path(offer.getPath()).agentId(offer.getAgentId())
                .isSent(false).request(request).build();
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println(sentOfferRepository.find(request.getId()));
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");
        if (sentOfferRepository.find(request.getId()) == 5){
            List<InlineKeyboardButton> keyboardButtonsRow= new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton()
                    .setText("Load..");
            button.setCallbackData("load");
            keyboardButtonsRow.add(button);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(keyboardButtonsRow);
            inlineKeyboardMarkup.setKeyboard(rowList);
            bot.execute(new SendMessage(request.getChatId(), "Do you want to load new Messages?")
                    .setReplyMarkup(inlineKeyboardMarkup));
        }
        else if(sentOfferRepository.find(request.getId()) < 5){
            Message message = bot.sendPhoto(request.getChatId(), offer.getPath());
            newOffer.setMessageId(message.getMessageId());
            newOffer.setIsSent(true);
            sentOfferRepository.save(request.getId());
        }
        requestRepository.save(request);
        repository.save(newOffer);
        return;
    }
}