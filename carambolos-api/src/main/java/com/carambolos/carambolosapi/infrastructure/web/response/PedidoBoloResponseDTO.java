package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoBoloEntity;
import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoBoloResponseDTO(
        Integer id,
        Integer enderecoId,
        Integer boloId,
        Integer usuarioId,
        String observacao,
        LocalDate dataPrevisaoEntrega,
        LocalDateTime dataUltimaAtualizacao,
        TipoEntregaEnum tipoEntrega,
        String nomeCliente,
        String telefoneCliente
) {

}
