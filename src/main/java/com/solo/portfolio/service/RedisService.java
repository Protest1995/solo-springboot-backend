package com.solo.portfolio.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

/**
 * Redis服務類
 * 提供通用的Redis操作方法
 */
@Service
public class RedisService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * 設置緩存
     * @param key 鍵
     * @param value 值
     * @param timeout 過期時間（秒）
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }
    
    /**
     * 獲取緩存
     * @param key 鍵
     * @return 緩存的值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    /**
     * 刪除緩存
     * @param key 鍵
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    
    /**
     * 檢查鍵是否存在
     * @param key 鍵
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    /**
     * 設置哈希表字段
     * @param key 鍵
     * @param hashKey 哈希表字段
     * @param value 值
     */
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }
    
    /**
     * 獲取哈希表字段值
     * @param key 鍵
     * @param hashKey 哈希表字段
     * @return 字段值
     */
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }
    
    /**
     * 遞增操作
     * @param key 鍵
     * @return 增加後的值
     */
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }
    
    /**
     * 設置過期時間
     * @param key 鍵
     * @param timeout 過期時間（秒）
     */
    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }
}
