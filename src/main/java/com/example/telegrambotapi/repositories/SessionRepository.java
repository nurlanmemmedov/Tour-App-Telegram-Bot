package com.example.telegrambotapi.repositories;

import com.example.telegrambotapi.models.Session;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * this class is used to interact with redis
 */
@Repository
public class SessionRepository {
    public static final String HASH_KEY = "Session";

    private RedisTemplate template;

    public SessionRepository(@Qualifier("redis") RedisTemplate template){
        this.template = template;
    }

    /**
     * save new session to redis
     * @param session
     * @return
     */
    public Session save(Session session){
        template.opsForHash().put(HASH_KEY,session.getClientId(),session);
        return session;
    }

    /**
     * find session by client id
     * @param clientId
     * @return
     */
    public Session find(Integer clientId){
        return (Session) template.opsForHash().get(HASH_KEY,clientId);
    }

    /**
     * delete session from redis by client id
     * @param clientId
     * @return
     */
    public String delete(Integer clientId){
        template.opsForHash().delete(HASH_KEY,clientId);
        return "Session removed !!";
    }

}
