package com.example.userorder.config;

import com.example.userorder.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Bean
    public JwtProvider jwtProvider(){
        return new JwtProvider();
    }
}
