package com.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/uploadImg/**").addResourceLocations("D:\\study resouce\\大三上\\Web 开发\\WebApplication_Springboot\\uploadImg");
        registry.addResourceHandler("**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/uploadImg/**").addResourceLocations("file:/home/yzx/Web/uploadImg");
    }

}

