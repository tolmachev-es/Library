package org.tolmachev.library.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tolmachev.library.service.DataStorage;


@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class RedisService implements DataStorage {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String DATA_KEY = "batch:data";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getBatchData() {
        log.info("getBatchData");

        return redisTemplate.opsForList().rightPop(DATA_KEY);
    }

    public void putBatchData(String value) {
        redisTemplate.opsForList().leftPush(DATA_KEY, value);
    }

    public boolean hasValue() {
        log.info("hasValue");
        Long size = redisTemplate.opsForList().size(DATA_KEY);
        log.info("size: {}", size);
        return size > 0;
    }
}
