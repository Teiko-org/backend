package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Cobertura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoberturaRepository extends JpaRepository<Cobertura, Integer> {
    Integer countByCorAndDescricaoIgnoreCase(String cor, String descricao);
    Integer countByCorAndDescricaoAndIdNot(String cor, String descricao, Integer id);
}
