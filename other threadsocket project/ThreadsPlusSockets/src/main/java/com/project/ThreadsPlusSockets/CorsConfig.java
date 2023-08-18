package com.project.ThreadsPlusSockets;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;


@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("*")//GET", "POST", "PUT", "DELETE"
                        .allowedHeaders("*")//"Content-Type"
                        .exposedHeaders("*",  "Access-Control-Allow-Origin")//"Content-Type"
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}