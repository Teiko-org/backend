package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Bolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoloRepository extends JpaRepository<Bolo,Integer> {
    Boolean existsByIdAndIsAtivoTrue(Integer id);
    Boolean existsByIdAndIdNotAndIsAtivoTrue(Integer id, Integer id2);
    List<Bolo> findByCategoriaIn(List<String> categoria);
}
