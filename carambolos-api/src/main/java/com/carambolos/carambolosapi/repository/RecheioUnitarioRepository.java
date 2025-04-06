package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.RecheioUnitario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecheioUnitarioRepository extends JpaRepository<RecheioUnitario, Integer> {
    public Integer countBySaborIgnoreCase(String sabor);
}
