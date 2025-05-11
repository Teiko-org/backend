package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Cobertura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoberturaRepository extends JpaRepository<Cobertura, Integer> {
    Integer countByCorAndDescricaoIgnoreCaseAndIsAtivoTrue(String cor, String descricao);
    Integer countByCorAndDescricaoAndIdNotAndIsAtivoTrue(String cor, String descricao, Integer id);
    Boolean existsByIdAndIsAtivoTrue(Integer id);
}
