package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ResumoPedidoRepository extends JpaRepository<ResumoPedido, Integer> {
   Page<ResumoPedido> findAllByIsAtivoTrue(Pageable pageable);
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
   List<ResumoPedido> findAllByIsAtivoTrueOrderByDataPedidoDesc();
   List<ResumoPedido> findByStatusIn(List<StatusEnum> status);
   List<ResumoPedido> findByStatusInOrderByDataPedidoDesc(List<StatusEnum> status);
   long countByStatusAndPedidoBoloIdIsNotNull(StatusEnum status);
   long countByPedidoBoloIdIsNotNull();
   long countByStatusAndPedidoFornadaIdIsNotNull(StatusEnum status);
   long countByPedidoFornadaIdIsNotNull();
   List<ResumoPedido> findByStatusInAndPedidoBoloIdIsNotNull(List<StatusEnum> status);
   List<ResumoPedido> findByStatusInAndPedidoFornadaIdIsNotNull(List<StatusEnum> status);
   List<ResumoPedido> findByStatusInAndIsAtivoTrue(List<StatusEnum> status);
   
   @Query(value = "SELECT * FROM resumo_pedido WHERE data_entrega BETWEEN :dataInicio AND :dataFim AND is_ativo = true ORDER BY data_entrega ASC", nativeQuery = true)
   List<ResumoPedido> findByDataEntregaBetweenAndIsAtivoTrueOrderByDataEntregaAsc(
       @Param("dataInicio") LocalDateTime dataInicio,
       @Param("dataFim") LocalDateTime dataFim
   );
}
