package com.carambolos.carambolosapi.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Libera o H2 Console
//                        .requestMatchers("/usuarios/**").permitAll() // Libera todas as rotas da API
                         .requestMatchers("/**").permitAll() // Libera tudo
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // Desativa CSRF (obrigatório para Postman e H2 Console)
                .headers(headers -> headers.frameOptions().disable()); // Permite o H2 funcionar corretamente

        return http.build();
    }
}