package com.openmpy.taleswiki.common.application;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void save(final String key, final Object value, final long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    public Object get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(final String key) {
        redisTemplate.delete(key);
    }
}
