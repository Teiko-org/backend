package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.domain.entity.JwtTokenBlacklist;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.JwtTokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenBlacklistService {
    @Autowired
    private JwtTokenBlacklistRepository blacklistRepository;

    public void blacklistToken(String token) {
        JwtTokenBlacklist entry = new JwtTokenBlacklist();
        entry.setToken(token);
        entry.setBlacklistedAt(LocalDateTime.now());
        blacklistRepository.save(entry);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistRepository.findByToken(token).isPresent();
    }
}
