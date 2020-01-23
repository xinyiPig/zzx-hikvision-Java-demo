package com.example.hkws.config;

import com.example.hkws.util.redis.FastJson2JsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 *
 * @Descrption redis配置
 * @class_name RedisConfig
 * @author Ergou
 * @Date 2018年9月7日 下午2:01:00
 */
@Configuration
public class RedisConfig {

    @SuppressWarnings("rawtypes")
    @Bean
    public RedisSerializer fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<Object>(Object.class);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer fastJson2JsonRedisSerializer) throws Exception {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
