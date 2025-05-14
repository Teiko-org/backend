package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Fornada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornadaRepository extends JpaRepository<Fornada, Integer> {
    boolean existsByIdAndIsAtivoTrue(Integer id);
}
