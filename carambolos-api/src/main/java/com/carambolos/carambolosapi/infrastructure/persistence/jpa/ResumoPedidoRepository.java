package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ResumoPedidoRepository extends JpaRepository<ResumoPedido, Integer> {
   List<ResumoPedido> findAllByIsAtivoTrue();
   Optional<ResumoPedido> findByIdAndIsAtivoTrue(Integer id);
   List<ResumoPedido> findByDataEntregaAndIsAtivoTrue(LocalDateTime dataEntrega);
   List<ResumoPedido> findByDataPedidoBetweenAndIsAtivoTrue(LocalDateTime dataInicio, LocalDateTime dataFim);
   List<ResumoPedido> findByPedidoBoloIdIsNotNullAndIsAtivoTrue();
   List<ResumoPedido> findByPedidoFornadaIdIsNotNullAndIsAtivoTrue();
   List<ResumoPedido> findByStatusAndIsAtivoTrue(StatusEnum status);
   boolean existsByIdAndIsAtivoTrue(Integer id);
   Optional<ResumoPedido> findTop1ByPedidoBoloIdAndIsAtivoTrueOrderByDataPedidoDesc(Integer id);
   Optional<ResumoPedido> findTop1ByPedidoFornadaIdAndIsAtivoTrueOrderByDataPedidoDesc(Integer id);
   long countByStatus(StatusEnum status);
   long countByStatusIn(List<StatusEnum> status);
   long count();
   List<ResumoPedido> findAllByOrderByDataPedidoDesc();
   List<ResumoPedido> findByStatusIn(List<StatusEnum> status);
   List<ResumoPedido> findByStatusInOrderByDataPedidoDesc(List<StatusEnum> status);
   long countByStatusAndPedidoBoloIdIsNotNull(StatusEnum status);
   long countByPedidoBoloIdIsNotNull();
   long countByStatusAndPedidoFornadaIdIsNotNull(StatusEnum status);
   long countByPedidoFornadaIdIsNotNull();
   List<ResumoPedido> findByStatusInAndPedidoBoloIdIsNotNull(List<StatusEnum> status);
   List<ResumoPedido> findByStatusInAndPedidoFornadaIdIsNotNull(List<StatusEnum> status);
}
