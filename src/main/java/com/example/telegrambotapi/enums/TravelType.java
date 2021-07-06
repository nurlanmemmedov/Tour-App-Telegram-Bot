package com.example.telegrambotapi.enums;

public enum TravelType {
    REST("rest"),
    EXCURSION("excursion"),
    EXTREME("extreme"),
    ANYTHING("does not matter");

    private final String type;

    TravelType(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
