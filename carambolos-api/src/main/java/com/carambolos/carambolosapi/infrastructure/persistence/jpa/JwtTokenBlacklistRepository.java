package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.domain.entity.JwtTokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenBlacklistRepository extends JpaRepository<JwtTokenBlacklist, Integer> {
    Optional<JwtTokenBlacklist> findByToken(String token);
}
