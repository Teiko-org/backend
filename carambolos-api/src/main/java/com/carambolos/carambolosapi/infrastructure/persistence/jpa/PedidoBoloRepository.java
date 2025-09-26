package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.domain.entity.PedidoBolo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoBoloRepository extends JpaRepository<PedidoBolo, Integer> {
    Boolean existsByIdAndIsAtivoTrue(Integer id);
}
