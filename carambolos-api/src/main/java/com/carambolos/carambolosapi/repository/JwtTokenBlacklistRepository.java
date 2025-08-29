package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.JwtTokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenBlacklistRepository extends JpaRepository<JwtTokenBlacklist, Integer> {
    Optional<JwtTokenBlacklist> findByToken(String token);
}
