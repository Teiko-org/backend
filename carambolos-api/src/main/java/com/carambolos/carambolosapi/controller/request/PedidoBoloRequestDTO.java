package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.PedidoBolo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PedidoBoloRequestDTO(
        @Schema(description = "ID do endereço", example = "1")
        @NotNull
        Integer enderecoId,

        @Schema(description = "ID do bolo", example = "1")
        @NotNull
        Integer boloId,

        @Schema(description = "ID do usuário (opcional)", example = "1")
        Integer usuarioId,

        @Schema(description = "Observação sobre o pedido", example = "Evitar açúcar")
        String observacao,

        @Schema(description = "Data prevista para entrega", example = "2023-12-25")
        @NotNull
        LocalDate dataPrevisaoEntrega,

        @Schema(description = "Data da última atualização", example = "2023-12-01T15:00:00")
        @NotNull
        LocalDateTime dataUltimaAtualizacao
) {
    public static PedidoBolo toPedidoBolo(PedidoBoloRequestDTO request) {
        if (request == null) {
            return null;
        }
        PedidoBolo pedidoBolo = new PedidoBolo();
        pedidoBolo.setEnderecoId(request.enderecoId);
        pedidoBolo.setBoloId(request.boloId);
        pedidoBolo.setUsuarioId(request.usuarioId);
        pedidoBolo.setObservacao(request.observacao);
        pedidoBolo.setDataPrevisaoEntrega(request.dataPrevisaoEntrega);
        pedidoBolo.setDataUltimaAtualizacao(request.dataUltimaAtualizacao);
        return pedidoBolo;
    }
}
