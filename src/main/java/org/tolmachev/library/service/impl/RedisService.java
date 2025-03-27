package org.tolmachev.library.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tolmachev.library.service.DataStorage;

import java.util.concurrent.TimeUnit;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class RedisService implements DataStorage {
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getBatchData() {
        log.info("getBatchData");
        String s;
        synchronized (redisTemplate) {
            s = redisTemplate.opsForList().rightPop("batch:data");
            redisTemplate.expire("batch:data", 0, TimeUnit.SECONDS);
        }
        return s;
    }

    public void putBatchData(String value) {
        redisTemplate.opsForList().leftPush("batch:data", value);
    }

    public boolean hasValue() {
        log.info("hasValue");
        Long size = redisTemplate.opsForList().size("batch:data");
        log.info("size: {}", size);
        return size > 0;
    }
}
