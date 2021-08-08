package com.example.telegrambotapi.repositories;

import com.example.telegrambotapi.models.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Integer> {
    Language getByName(String language);
}
