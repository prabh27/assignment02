package com.cnu2016;

import com.cnu2016.controller.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@SpringBootApplication
public class Application extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter{

    @Autowired
    RequestInterceptor requestInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}