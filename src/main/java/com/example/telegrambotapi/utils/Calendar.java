package com.example.telegrambotapi.utils;

import com.example.telegrambotapi.models.KeyboardButton;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

import org.joda.time.LocalDate;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Calendar {

    public static final String IGNORE = "ignore!@#$%^&";

    public static final String[] WD = {"M", "T", "W", "T", "F", "S", "S"};

    public static InlineKeyboardMarkup generateKeyboard(LocalDate date) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        if (date == null) return null;

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // row - Month and Year
        List<InlineKeyboardButton> headerRow = new ArrayList<>();
        headerRow.add(createButton(IGNORE, new SimpleDateFormat("MMM yyyy").format(date.toDate())));
        keyboard.add(headerRow);

        // row - Days of the week
        List<InlineKeyboardButton> daysOfWeekRow = new ArrayList<>();
        for (String day : WD) {
            daysOfWeekRow.add(createButton(IGNORE, day));
        }
        keyboard.add(daysOfWeekRow);

        LocalDate firstDay = date.dayOfMonth().withMinimumValue();

        int shift = firstDay.dayOfWeek().get() - 1;
        int daysInMonth = firstDay.dayOfMonth().getMaximumValue();
        int rows = ((daysInMonth + shift) % 7 > 0 ? 1 : 0) + (daysInMonth + shift) / 7;
        for (int i = 0; i < rows; i++) {
            keyboard.add(buildRow(firstDay, shift));
            firstDay = firstDay.plusDays(7 - shift);
            shift = 0;
        }

        List<InlineKeyboardButton> controlsRow = new ArrayList<>();
        if (date.getMonthOfYear() == LocalDate.now().getMonthOfYear()){
            controlsRow.add(createButton(">", ">"));
        }else{
            controlsRow.add(createButton("<", "<"));
            controlsRow.add(createButton(">", ">"));
        }
        keyboard.add(controlsRow);
        markup.setKeyboard(keyboard);
        return markup;
    }

    private static InlineKeyboardButton createButton(String callBack, String text) {

        return new InlineKeyboardButton().setCallbackData(callBack).setText(text);
    }

    private static List<InlineKeyboardButton> buildRow(LocalDate date, int shift) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        int day = date.getDayOfMonth();


        LocalDate callbackDate = date;
        for (int j = 0; j < shift; j++) {
            row.add(createButton(IGNORE, " "));
        }
        for (int j = shift; j < 7; j++) {
            if (day <= (date.dayOfMonth().getMaximumValue())) {
                if (date.getYear() == LocalDate.now().getYear() && date.getMonthOfYear() == LocalDate.now().getMonthOfYear()){
                    if (day == LocalDate.now().getDayOfMonth()){
                        row.add(createButton(callbackDate.toString(), "\uD83D\uDCC5"));
                    }else if(day < LocalDate.now().getDayOfMonth()){
                        row.add(createButton(callbackDate.toString(), "\uD83D\uDEAB"));
                    }
                    else {
                        row.add(createButton(callbackDate.toString(), Integer.toString(day)));
                    }
                }
                else {
                    row.add(createButton(callbackDate.toString(), Integer.toString(day)));
                }
                day++;
                callbackDate = callbackDate.plusDays(1);
            } else {
                row.add(createButton(IGNORE, " "));
            }
        }
        return row;
    }
}