package com.devsz.studenteventsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

     @Bean
     public WebMvcConfigurer corConfigurer() {
          return new WebMvcConfigurer() {
               @Override
               public void addCorsMappings(CorsRegistry registry) {
                   registry.addMapping("/**")
                           .allowedOrigins("*")//http://localhost:8100
                           .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
               }
           };
     }

}
