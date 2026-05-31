package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoBoloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PedidoBoloRepository extends JpaRepository<PedidoBoloEntity, Integer> {
    Boolean existsByIdAndIsAtivoTrue(Integer id);
    List<PedidoBoloEntity> findAllByIsAtivoTrueAndDataPrevisaoEntregaBetween(LocalDate dataInicio, LocalDate dataFim);

    @Query("select p from pedido_bolo p where p.isAtivo = true and month(p.dataPrevisaoEntrega) = :mes")
    List<PedidoBoloEntity> findAllByIsAtivoTrueAndDataPrevisaoEntregaMonth(@Param("mes") Integer mes);
}
