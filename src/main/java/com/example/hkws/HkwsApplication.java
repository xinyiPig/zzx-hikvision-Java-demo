package com.example.hkws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@EnableRedisHttpSession
@Configuration
@EnableCaching
public class HkwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HkwsApplication.class, args);
    }

}
