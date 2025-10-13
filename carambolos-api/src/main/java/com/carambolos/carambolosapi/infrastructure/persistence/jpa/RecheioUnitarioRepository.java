package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioUnitarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecheioUnitarioRepository extends JpaRepository<RecheioUnitarioEntity, Integer> {
    Integer countBySaborIgnoreCaseAndIdNotAndIsAtivo(String sabor, Integer id, boolean isAtivo);
    Integer countBySaborIgnoreCaseAndIsAtivo(String sabor, Boolean isAtivo);
    Boolean existsByIdAndIsAtivoTrue(Integer id);
}
