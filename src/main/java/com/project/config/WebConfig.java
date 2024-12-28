package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS to all endpoints
                .allowedOrigins("http://localhost:3000") // Frontend origin
                .allowedOrigins("http://192.168.29.30:3000") // Frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true) // Allows sending cookies or credentials
                .maxAge(3600); // Cache preflight results for 1 hour
    }
}
