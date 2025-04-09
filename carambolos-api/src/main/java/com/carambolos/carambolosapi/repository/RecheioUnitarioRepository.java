package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.RecheioUnitario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecheioUnitarioRepository extends JpaRepository<RecheioUnitario, Integer> {
    Integer countBySaborIgnoreCaseAndIdNot(String sabor, Integer id);
    Integer countBySaborIgnoreCase(String sabor);
}
