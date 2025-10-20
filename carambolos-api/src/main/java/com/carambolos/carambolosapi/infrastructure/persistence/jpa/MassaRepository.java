package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.MassaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MassaRepository extends JpaRepository<MassaEntity, Integer> {
    Integer countBySaborAndIsAtivo(String sabor, Boolean isAtivo);
    Integer countBySaborAndIdNotAndIsAtivo(String sabor, Integer id, Boolean isAtivo);
    Boolean existsByIdAndIsAtivo(Integer id, Boolean isAtivo);
}
