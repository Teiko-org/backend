package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PedidoBoloCompletoResponseDTO(
        Integer id,
        BoloCompletoResponseDTO bolo,
        EnderecoResponseDTO endereco,
        UsuarioResponseDTO usuario,
        StatusEnum status,
        String observacao,
        LocalDate dataPrevisaoEntrega,
        LocalDateTime dataUltimaAtualizacao,
        TipoEntregaEnum tipoEntrega,
        String nomeCliente,
        String telefoneCliente
) {
}

