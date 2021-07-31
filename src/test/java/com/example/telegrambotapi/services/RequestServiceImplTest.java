package com.example.telegrambotapi.services;

import com.example.telegrambotapi.models.entities.Request;
import com.example.telegrambotapi.repositories.RequestRepository;
import com.example.telegrambotapi.services.interfaces.RequestService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RequestServiceImplTest {

    @Autowired
    private RequestService service;

    @Autowired
    private RequestRepository repository;

    @Test
    @Order(1)
    @Transactional
    @DisplayName("RequestService -> Get By uuid")
    void getByUuid() {
        Assertions.assertEquals(service.getByUuid("456feuc2-aayc-45c4-kdeb-as91afd7c076").getUuid(), "456feuc2-aayc-45c4-kdeb-as91afd7c076");
    }

    @Test
    @Order(2)
    @Transactional
    @DisplayName("RequestService -> Find by client id")
    void findByClientId() {
        Assertions.assertEquals(service.findByClientId(1231).size(), 1);
    }


    @Test
    @Order(3)
    @Transactional
    @DisplayName("RequestService -> Save")
    void save() {
        service.save(Request.builder().clientId(1231).isActive(true).uuid("9iaseuc2-bbyc-45c4-asdb-9991afd7c076").chatId(999324l).build());
        Assertions.assertEquals(repository.findAll().size(), 4);
    }

    @BeforeAll
    public void init() {
        List<Request> requests = new ArrayList<>();
        requests.add(Request.builder().clientId(1231).isActive(true).uuid("456feuc2-aayc-45c4-kdeb-as91afd7c076").chatId(123324l).build());
        requests.add(Request.builder().clientId(1331).isActive(true).uuid("996feuc2-aayc-45c4-12deb-as91afd7f76").chatId(131324l).build());
        requests.add(Request.builder().clientId(1431).isActive(true).uuid("ds3feuc2-aayc-45c4-laab-as91afd7c076").chatId(128924l).build());
        repository.saveAllAndFlush(requests);
    }


    @AfterAll
    public void clean() {
        repository.deleteAll();
    }

}