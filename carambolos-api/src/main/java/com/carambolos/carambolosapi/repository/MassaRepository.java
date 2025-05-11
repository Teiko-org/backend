package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Massa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MassaRepository extends JpaRepository<Massa, Integer> {
    Integer countBySaborAndIsAtivo(String sabor, Boolean isAtivo);
    Integer countBySaborAndIdNotAndIsAtivo(String sabor, Integer id, Boolean isAtivo);
    Boolean existsByIdAndIsAtivo(Integer id, Boolean isAtivo);
}
