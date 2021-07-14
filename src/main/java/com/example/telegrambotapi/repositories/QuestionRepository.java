package com.example.telegrambotapi.repositories;

import com.example.telegrambotapi.models.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question getQuestionByQuestionKey(String key);
}
