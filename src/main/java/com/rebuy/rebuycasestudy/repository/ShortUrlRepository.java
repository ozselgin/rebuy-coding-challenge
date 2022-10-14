package com.rebuy.rebuycasestudy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ShortUrlRepository {
    public static final String HASH_KEY_NAME = "SHORT-URL";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void save(String productUrlHash, String fullUrl){
        redisTemplate.opsForHash().put(HASH_KEY_NAME, productUrlHash, fullUrl);
    }

    public Optional<String> findUrlByHash(String productUrlHash){
        return Optional.ofNullable((String) redisTemplate.opsForHash().get(HASH_KEY_NAME,productUrlHash));
    }

}
