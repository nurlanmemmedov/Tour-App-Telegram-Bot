package com.example.telegrambotapi.repositories;

import com.example.telegrambotapi.dtos.SelectedOfferDto;
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
public class SelectionRepository {
    public static final String HASH_KEY = "Selection";

    private RedisTemplate template;

    public SelectionRepository(@Qualifier("redis") RedisTemplate template){
        this.template = template;
    }

    /**
     * save new selected offer to redis
     * @param offerDto
     * @return
     */
    public SelectedOfferDto save(SelectedOfferDto offerDto){
        template.opsForHash().put(HASH_KEY,offerDto.getClientId(),offerDto);
        return offerDto;
    }

    /**
     * find selected offer by client id
     * @param clientId
     * @return
     */
    public SelectedOfferDto find(Integer clientId){
        return (SelectedOfferDto) template.opsForHash().get(HASH_KEY,clientId);
    }

    /**
     * delete selected offer from redis by client id
     * @param clientId
     * @return
     */
    public String delete(Integer clientId){
        template.opsForHash().delete(HASH_KEY,clientId);
        return "Selection removed !!";
    }

}
