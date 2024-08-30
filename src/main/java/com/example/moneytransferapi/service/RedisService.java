package com.example.moneytransferapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void storeValue(String key, String value, int TTLSeconds) {
        redisTemplate.opsForValue().set(key, "0", TTLSeconds, TimeUnit.SECONDS);
    }

    public boolean keyExists(String key) {
        Long expireTime = redisTemplate.getExpire("b:"+key, TimeUnit.SECONDS);
        return expireTime != null && expireTime != -2;
    }
}
