package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.application.usecases.TokenBlacklistService;
import com.carambolos.carambolosapi.application.usecases.UsuarioUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.UsuarioGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.UsuarioMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.UsuarioRepository;
import com.carambolos.carambolosapi.system.security.GerenciadorTokenJwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioConfig {

    @Bean
    UsuarioUseCase createUserCase(UsuarioGateway usuarioGateway, PasswordEncoder passwordEncoder, GerenciadorTokenJwt gerenciadorTokenJwt, AuthenticationManager authenticationManager, TokenBlacklistService tokenBlacklistService) {
        return new UsuarioUseCase(usuarioGateway, passwordEncoder, gerenciadorTokenJwt, authenticationManager, tokenBlacklistService);
    }

    @Bean
    UsuarioGateway usuarioGateway(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        return new UsuarioGatewayImpl(usuarioRepository, usuarioMapper);
    }

    @Bean
    UsuarioMapper usuarioMapper() {
        return new UsuarioMapper();
    }

}
