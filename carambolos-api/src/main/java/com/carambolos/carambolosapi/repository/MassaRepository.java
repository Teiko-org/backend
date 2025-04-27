package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Massa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MassaRepository extends JpaRepository<Massa, Integer> {
    Integer countBySabor(String sabor);
    Integer countBySaborAndIdNot(String sabor, Integer id);
}
