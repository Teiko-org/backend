package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoFornadaRepository extends JpaRepository<PedidoFornada, Integer> {
    boolean existsByIdAndIsAtivoTrue(Integer id);
}
