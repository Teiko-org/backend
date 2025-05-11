package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.RecheioUnitario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecheioUnitarioRepository extends JpaRepository<RecheioUnitario, Integer> {
    Integer countBySaborIgnoreCaseAndIdNotAndIsAtivo(String sabor, Integer id, boolean isAtivo);
    Integer countBySaborIgnoreCaseAndIsAtivo(String sabor, Boolean isAtivo);
    Boolean existsByIdAndIsAtivoTrue(Integer id);
}
