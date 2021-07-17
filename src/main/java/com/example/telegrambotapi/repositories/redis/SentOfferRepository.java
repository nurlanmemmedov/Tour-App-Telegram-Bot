package com.example.telegrambotapi.repositories.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * this class is used to interact with redis
 */
@Repository
public class SentOfferRepository {
    public static final String HASH_KEY = "Sent";

    private RedisTemplate template;

    public SentOfferRepository(@Qualifier("redis") RedisTemplate template){
        this.template = template;
    }

    /**
     * saves and increases the count of sent offer in redis
     * @param requestId
     * @return
     */
    public Integer save(Integer requestId){
        Integer count = find(requestId);
        if (count == null){
            count = 1;
        }else{
            count += 1;
        }
        template.opsForHash().put(HASH_KEY,requestId,count);
        return count;
    }

    /**
     * finds count of sent offer by request id
     * @param requestId
     * @return
     */
    public Integer find(Integer requestId){
        Integer count =  (Integer) template.opsForHash().get(HASH_KEY,requestId);
        if (count == null) return 0;
        return count;
    }

    /**
     * deletes count of sent offer from redis by request id
     * @param requestId
     * @return
     */
    public String delete(Integer requestId){
        template.opsForHash().delete(HASH_KEY,requestId);
        return "Offer removed !!";
    }

}
