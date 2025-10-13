package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.CoberturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoberturaRepository extends JpaRepository<CoberturaEntity, Integer> {
    Integer countByCorAndDescricaoIgnoreCaseAndIsAtivoTrue(String cor, String descricao);
    Integer countByCorAndDescricaoAndIdNotAndIsAtivoTrue(String cor, String descricao, Integer id);
    Boolean existsByIdAndIsAtivoTrue(Integer id);
}
