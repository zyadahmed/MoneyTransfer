package com.example.moneytransferapi.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;




@AllArgsConstructor
@Service
public class SessionService {

    private final RedisService redisService;
    private static final int MAX_INACTIVITY_TIME_SECONDS = 120;

    public boolean stillActive(String token) {
        String tokenPrefix = "S:";
        String key = tokenPrefix + token;

        Long remainingTime = redisService.getExpire(key);

        if (remainingTime == null || remainingTime == -2) {
            return false;
        }

        if (remainingTime < MAX_INACTIVITY_TIME_SECONDS) {
            redisService.storeValue(key, "0", MAX_INACTIVITY_TIME_SECONDS);
            return true;
        } else {
            return false;
        }
    }
    public void createSession(String token) {
        String tokenPrefix = "S:";
        String key = tokenPrefix + token;
        redisService.storeValue(key, "0", MAX_INACTIVITY_TIME_SECONDS);
    }
}

