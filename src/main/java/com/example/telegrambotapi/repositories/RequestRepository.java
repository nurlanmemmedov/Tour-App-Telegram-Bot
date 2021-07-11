package com.example.telegrambotapi.repositories;

import com.example.telegrambotapi.models.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByClientId(Integer clientId);
}
