package com.solo.portfolio.service.cache;

import com.solo.portfolio.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserCacheService {
    private static final String CACHE_KEY_PREFIX = "user:";
    private static final long CACHE_TTL_HOURS = 24;

    private final RedisTemplate<String, User> redisTemplate;

    @Autowired
    public UserCacheService(RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<User> get(String username) {
        try {
            User user = redisTemplate.opsForValue().get(CACHE_KEY_PREFIX + username);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            // 如果發生序列化錯誤，則返回空
            return Optional.empty();
        }
    }

    public void put(User user) {
        try {
            redisTemplate.opsForValue().set(
                CACHE_KEY_PREFIX + user.getUsername(),
                user,
                CACHE_TTL_HOURS,
                TimeUnit.HOURS
            );
        } catch (Exception e) {
            // 忽略序列化錯誤
        }
    }

    public void delete(String username) {
        redisTemplate.delete(CACHE_KEY_PREFIX + username);
    }
}
