package com.springboot.sqlinjectionlab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置uploads目录为静态资源目录
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // 如果默认头像文件放在static目录下，也可以这样配置
        // registry.addResourceHandler("/static/**")
        //         .addResourceLocations("classpath:/static/");
    }
}