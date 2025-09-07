package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.PedidoBolo;
import com.carambolos.carambolosapi.model.enums.TipoEntregaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PedidoBoloRequestDTO(
        @Schema(description = "ID do endereço", example = "1")
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
        LocalDateTime dataUltimaAtualizacao,

        @Schema(description = "Tipo de entrega", example = "RETIRADA", allowableValues = "RETIRADA, ENTREGA")
        TipoEntregaEnum tipoEntrega,

        @Schema(description = "Nome do cliente", example = "João da Silva")
        @NotBlank
        String nomeCliente,

        @Schema(description = "Telefone do cliente", example = "(11) 91234-5678")
        @NotBlank
        String telefoneCliente,

        @Schema(description = "Horário de retirada (apenas para tipo RETIRADA)", example = "17:00")
        String horarioRetirada
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
        pedidoBolo.setTipoEntrega(request.tipoEntrega);
        pedidoBolo.setNomeCliente(request.nomeCliente);
        pedidoBolo.setTelefoneCliente(request.telefoneCliente);
        pedidoBolo.setHorarioRetirada(request.horarioRetirada);
        return pedidoBolo;
    }
}
