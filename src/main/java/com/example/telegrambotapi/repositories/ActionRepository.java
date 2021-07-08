package com.example.telegrambotapi.repositories;

import com.example.telegrambotapi.models.Action;
import com.example.telegrambotapi.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Integer> {
    List<Action> getAllByQuestionId(Integer id);
}