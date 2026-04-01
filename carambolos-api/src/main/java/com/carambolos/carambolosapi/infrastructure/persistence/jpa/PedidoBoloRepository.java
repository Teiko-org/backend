package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoBoloEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoBoloRepository extends JpaRepository<PedidoBoloEntity, Integer> {
    Boolean existsByIdAndIsAtivoTrue(Integer id);
    List<PedidoBoloEntity> findAllByIsAtivoTrueAndDataUltimaAtualizacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
}
