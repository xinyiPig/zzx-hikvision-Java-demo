package com.example.hkws.util.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redicache 工具类
 *
 */
@SuppressWarnings("unchecked")
@Component
public class RedisTemplateUtil {
    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;
    
    /**
     * 	批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }
    /**
     * 	批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }
    /**
     * 	删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 	判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    /**
     * 	读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
       // redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        if(result==null){
            return null;
        }
        return result;
    }
    /**
     * 	写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 	写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Integer expire) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public  boolean hmset(String key, Map<String, String> value) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().putAll(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public  Map<String,String> hmget(String key) {
        Map<String,String> result =null;
        try {
            result=  redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 	整数自增
     *
     * @param key
     * @param
     * @return
     */
    public Long incr(final String key) {
        Long result = 1L;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            result = operations.increment(key,1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 	刷新过期时间，实际上就是删除再新建  -00-!!!
     * @param key
     * @param timestamp
     */
    public void refreshExpire(final String key, Object value, Integer expire) {
    	if (exists(key)) {
            redisTemplate.delete(key);
        }
    	set(key, value, expire);
    }
}