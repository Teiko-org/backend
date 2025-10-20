package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoBoloEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoBoloRepository extends JpaRepository<PedidoBoloEntity, Integer> {
    Boolean existsByIdAndIsAtivoTrue(Integer id);
}
