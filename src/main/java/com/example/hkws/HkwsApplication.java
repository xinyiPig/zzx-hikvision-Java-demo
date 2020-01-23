package com.example.hkws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
@Configuration
@EnableCaching
public class HkwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HkwsApplication.class, args);
    }

}
