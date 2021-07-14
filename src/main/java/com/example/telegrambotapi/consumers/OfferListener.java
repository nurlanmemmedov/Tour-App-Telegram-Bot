package com.example.telegrambotapi.consumers;

import com.example.telegrambotapi.bot.TourBot;
import com.example.telegrambotapi.configs.RabbitmqConfig;
import com.example.telegrambotapi.models.entities.Offer;
import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.repositories.OfferRepository;
import com.example.telegrambotapi.repositories.RequestRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OfferListener {

    private OfferRepository repository;
    private RequestRepository requestRepository;
    private TourBot bot;

    public OfferListener(OfferRepository repository, TourBot bot,
                         RequestRepository requestRepository){
        this.repository = repository;
        this.requestRepository = requestRepository;
        this.bot = bot;
    }

    @RabbitListener(queues = RabbitmqConfig.OFFERQUEUE)
    public void consumeMessageFromQueue(Offer offer) throws TelegramApiException {
        Request request = requestRepository.getRequestByUuid(offer.getUuid());
        if (request == null) return; //TODO
        Offer newOffer = Offer.builder().uuid(offer.getUuid())
                .path(offer.getPath()).isSent(false).request(request).build();

        if (request.getHasNext() && request.getOffers().size() % 5 == 0){
            request.setHasNext(false);

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
        if (request.getHasNext()){
            bot.sendPhoto(request.getChatId(), offer.getPath());
            newOffer.setIsSent(true);
        }
        requestRepository.save(request);
        repository.save(newOffer);
        return;
    }
}