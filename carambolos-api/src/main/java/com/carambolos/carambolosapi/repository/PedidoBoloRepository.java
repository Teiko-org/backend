package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.PedidoBolo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoBoloRepository extends JpaRepository<PedidoBolo, Integer> {
    Boolean existsByIdAndIsAtivoTrue(Integer id);
    Boolean existsByIdAndIdNotAndIsAtivoTrue(Integer id, Integer id2);
    Integer countByEnderecoIdAndUsuarioIdAndIsAtivoTrue(Integer enderecoId, Integer usuarioId);
    Integer countByEnderecoIdAndUsuarioIdAndIdNotAndIsAtivoTrue(Integer enderecoId, Integer usuarioId, Integer id);
}
