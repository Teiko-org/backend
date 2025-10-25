package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.ResumoPedidoEntity;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ResumoPedidoRepository extends JpaRepository<ResumoPedidoEntity, Integer> {
   List<ResumoPedidoEntity> findAllByIsAtivoTrue();
   Optional<ResumoPedidoEntity> findByIdAndIsAtivoTrue(Integer id);
   List<ResumoPedidoEntity> findByDataEntregaAndIsAtivoTrue(LocalDateTime dataEntrega);
   List<ResumoPedidoEntity> findByDataPedidoBetweenAndIsAtivoTrue(LocalDateTime dataInicio, LocalDateTime dataFim);
   List<ResumoPedidoEntity> findByPedidoBoloIdIsNotNullAndIsAtivoTrue();
   List<ResumoPedidoEntity> findByPedidoFornadaIdIsNotNullAndIsAtivoTrue();
   List<ResumoPedidoEntity> findByStatusAndIsAtivoTrue(StatusEnum status);
   boolean existsByIdAndIsAtivoTrue(Integer id);
   Optional<ResumoPedidoEntity> findTop1ByPedidoBoloIdAndIsAtivoTrueOrderByDataPedidoDesc(Integer id);
   Optional<ResumoPedidoEntity> findTop1ByPedidoFornadaIdAndIsAtivoTrueOrderByDataPedidoDesc(Integer id);
   long countByStatus(StatusEnum status);
   long countByStatusIn(List<StatusEnum> status);
   long count();
   List<ResumoPedidoEntity> findAllByOrderByDataPedidoDesc();
   List<ResumoPedidoEntity> findByStatusIn(List<StatusEnum> status);
   List<ResumoPedidoEntity> findByStatusInOrderByDataPedidoDesc(List<StatusEnum> status);
   long countByStatusAndPedidoBoloIdIsNotNull(StatusEnum status);
   long countByPedidoBoloIdIsNotNull();
   long countByStatusAndPedidoFornadaIdIsNotNull(StatusEnum status);
   long countByPedidoFornadaIdIsNotNull();
   List<ResumoPedidoEntity> findByStatusInAndPedidoBoloIdIsNotNull(List<StatusEnum> status);
   List<ResumoPedidoEntity> findByStatusInAndPedidoFornadaIdIsNotNull(List<StatusEnum> status);
}
