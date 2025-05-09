package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Bolo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoloRepository extends JpaRepository<Bolo,Integer> {
    Boolean existsByIdAndIsAtivoTrue(Integer id);
    Boolean existsByIdAndIdNotAndIsAtivoTrue(Integer id, Integer id2);


}
