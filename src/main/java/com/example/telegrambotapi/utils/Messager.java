package com.example.telegrambotapi.utils;

public class Messager {
    public static String incorrectAnswer(String languageCode){
        if (languageCode == null) return "Yanlış cavab!";
        if (languageCode.equals("En")) return "Incorrect answer!";
        if (languageCode.equals("Ru")) return "Неправильный ответ!";
        return "Yanlış cavab!";
    }

    public static String incorrectCommand(String languageCode){
        if (languageCode == null) return "Yanlış komanda";
        if (languageCode.equals("En")) return "Incorrect command";
        if (languageCode.equals("Ru")) return "Неверная команда";
        return "Yanlış komanda";
    }

    public static String startMessage(String languageCode){
        if (languageCode == null) return "Başlamaq üçün /start komandasını daxil edin";
        if (languageCode.equals("En")) return "Please type /start to start";
        if (languageCode.equals("Ru")) return "Пожалуйста, введите /start, чтобы начать";
        return "Başlamaq üçün /start komandasını daxil edin";
    }

    public static String stopMessage(String languageCode){
        if (languageCode == null) return "Sizin sessiyanız silindi, yenidən başlamaq üçün /start komandasını daxil edin";
        if (languageCode.equals("En")) return "Your session was removed, you can restart with typing /start";
        if (languageCode.equals("Ru")) return "Ваш сеанс был удален, вы можете перезапустить его, набрав /start";
        return "Sizin sessiyanız silindi, yenidən başlamaq üçün /start komandasını daxil edin";
    }

    public static String expireMessage(String languageCode){
        if (languageCode == null) return "Sizin sessiyanızın müddəti bitdi, Yeni anketə başlamaq üçün /start komandasını daxil edin.";
        if (languageCode.equals("En")) return "Your session was expired, type /start to start a new survey";
        if (languageCode.equals("Ru")) return "Срок действия вашей сессии истек, введите /start, чтобы начать новый опрос";
        return "Sizin sessiyanızın müddəti bitdi, Yeni anketə başlamaq üçün /start komandasını daxil edin.";
    }

    public static String activeSessionMessage(String languageCode){
        if (languageCode == null) return "Sizin aktiv sessiyanız var, yenidən başlamaq üçün ilk öncə /stop komandasını daxil edin";
        if (languageCode.equals("En")) return "You have active session, please first type /stop to restart";
        if (languageCode.equals("Ru")) return "У вас активный сеанс, введите /stop, чтобы перезапустить";
        return "Sizin aktiv sessiyanız var, yenidən başlamaq üçün ilk öncə /stop komandasını daxil edin";
    }

    public static String sessionMessage(String languageCode){
        if (languageCode == null) return "Sizin aktiv sessiyanız yoxdur, zəhmət olmasa başlamaq üçün /start komandasını daxil edin";
        if (languageCode.equals("En")) return "You don't have active session, please type /start to start";
        if (languageCode.equals("Ru")) return "У вас нет активного сеанса, введите /start, чтобы начать";
        return "Sizin aktiv sessiyanız yoxdur, zəhmət olmasa başlamaq üçün /start komandasını daxil edin";
    }

    public static String offerCaption(String languageCode){
        if (languageCode == null) return "Təklifi seçmək üçün şəkilə reply edib, 'yes' daxil edin";
        if (languageCode.equals("En")) return "To select an offer, reply to the image and enter 'yes'";
        if (languageCode.equals("Ru")) return "Чтобы выбрать предложение, ответьте на изображение и введите 'yes'";
        return "Təklifi seçmək üçün şəkilə reply edib, 'yes' daxil edin";
    }

    public static String selectionMessage(String languageCode){
        if (languageCode == null) return "Əla, Seçiminiz uğurla göndərildi";
        if (languageCode.equals("En")) return "Excellent, Your Selection was sent successfully";
        if (languageCode.equals("Ru")) return "Отлично, ваш выбор был успешно отправлен";
        return "Əla, Seçiminiz uğurla göndərildi";
    }

    public static String loadOfferQuestion(String languageCode){
        if (languageCode == null) return "Yeni təkliflər görmək istəyirsinizmi?";
        if (languageCode.equals("En")) return "Do you want to load new offers?";
        if (languageCode.equals("Ru")) return "Хотите загрузить новые предложения?";
        return "Yeni təkliflər görmək istəyirsinizmi?";
    }

    public static String loadOfferAction(String languageCode){
        if (languageCode == null) return "Yüklə...";
        if (languageCode.equals("En")) return "Load...";
        if (languageCode.equals("Ru")) return "Загрузить...";
        return "Yüklə...";
    }
}
