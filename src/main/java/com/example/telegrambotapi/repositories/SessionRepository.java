package com.example.telegrambotapi.repositories;

import com.example.telegrambotapi.models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SessionRepository {
    public static final String HASH_KEY = "Session";
    public static final String ACTIVE_SESSION_KEY = "ActiveSession";

    @Autowired
    private RedisTemplate template;

    public Session saveSession(Session session){
        template.opsForHash().put(HASH_KEY,session.getUuid(),session);
        return session;
    }

    public List<Session> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }

    public Session findSessionUuid(String uuid){
        return (Session) template.opsForHash().get(HASH_KEY,uuid);
    }

    public String deleteSession(String uuid){
        template.opsForHash().delete(HASH_KEY,uuid);
        return "Session removed !!";
    }

    public Session saveActiveSession(Session session){
        template.opsForHash().put(ACTIVE_SESSION_KEY,session.getClientId(),session);
        return session;
    }

    public Session findActiveSessionUuid(Integer clientId){
        return (Session) template.opsForHash().get(ACTIVE_SESSION_KEY,clientId);
    }

    public String deleteActiveSession(Integer clientId){
        template.opsForHash().delete(HASH_KEY,clientId);
        return "Active Session removed !!";
    }

}
