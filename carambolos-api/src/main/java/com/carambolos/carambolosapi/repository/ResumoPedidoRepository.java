package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.ResumoPedido;
import com.carambolos.carambolosapi.model.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
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

}
