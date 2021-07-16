package com.example.telegrambotapi.repositories;

import com.example.telegrambotapi.models.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Integer> {
    Offer getByMessageId(Integer messageId);
}
