package com.example.telegrambotapi.repositories;

import com.example.telegrambotapi.models.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByClientId(Integer clientId);
    Request getRequestByUuid(String uuid);
    @Query(value = "SELECT * FROM requests WHERE expire_date < now() AND is_active = TRUE ", nativeQuery = true)
    List<Request> getAllExpiredRequests();

    @Modifying
    @Transactional
    @Query(value = "UPDATE requests SET is_active = FALSE WHERE expire_date IS NOT NULL AND expire_date < now() AND is_active = TRUE", nativeQuery = true)
    void updateExpiredRequests();
}
