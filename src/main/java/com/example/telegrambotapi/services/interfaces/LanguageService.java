package com.example.telegrambotapi.services.interfaces;

import com.example.telegrambotapi.models.entities.Language;

public interface LanguageService {
    Language getByName(String language);
}
