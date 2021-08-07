package com.example.telegrambotapi.services;

import com.example.telegrambotapi.models.entities.Language;
import com.example.telegrambotapi.repositories.LanguageRepository;
import com.example.telegrambotapi.services.interfaces.LanguageService;
import org.springframework.stereotype.Service;

@Service
public class LanguageServiceImpl implements LanguageService {
    private LanguageRepository repository;

    public LanguageServiceImpl(LanguageRepository repository){
        this.repository = repository;
    }

    @Override
    public Language getByName(String language) {
        return repository.getByName(language);
    }
}
