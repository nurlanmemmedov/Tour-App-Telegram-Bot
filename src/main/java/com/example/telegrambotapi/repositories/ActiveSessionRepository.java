package com.example.telegrambotapi.repositories;

import com.example.telegrambotapi.models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class ActiveSessionRepository {
    public static final String HASH_KEY = "ActiveSession";
    @Autowired
    private RedisTemplate template;

    public Session save(Session session){
        template.opsForHash().put(HASH_KEY,session.getUuid(),session);
        return session;
    }

    public List<Session> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }

    public Session findProductByUuid(String uuid){
        return (Session) template.opsForHash().get(HASH_KEY,uuid);
    }

    public String deleteProduct(String uuid){
        template.opsForHash().delete(HASH_KEY,uuid);
        return "Session removed !!";
    }
}
