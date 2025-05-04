package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Bolo;
import com.carambolos.carambolosapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoloRepository extends JpaRepository<Bolo,Integer> {
    Boolean existsByIdAndIsAtivoTrue(Integer id);

}
