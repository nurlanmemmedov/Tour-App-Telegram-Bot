package com.example.telegrambotapi.dtos;

import com.example.telegrambotapi.enums.Language;
import com.example.telegrambotapi.enums.TravelType;

import java.util.Date;

public class TourRequestDto {
    private Language language;
    private TravelType travelType;
    private String departureAddress;
    private String destinationAddress;
    private int quantity;
    private Date travelDate;
    private int price;
}
