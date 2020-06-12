package com.example.hkws.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置文件路径映射
 * @Description:
 * @Param:
 * @Author: Mr.Gdd
 * @Date: 2019/9/24
 * @Time: 15:55
 * @return:
 */
@Configuration
@Slf4j
public class MappingsConfig implements WebMvcConfigurer {


    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        log.info("文件存放路径：" + uploadPath);

        registry.addResourceHandler("/file/**").addResourceLocations("file:" + uploadPath + "/");

        log.info("自定义静态资源目录、此处功能用于文件映射");

    }
}