package com.carambolos.carambolosapi.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO para solicitar mensagem consolidada de múltiplos resumos de pedido")
public record MensagensResumoRequestDTO(
        @Schema(description = "Lista de IDs de ResumoPedido a consolidar", example = "[1,2,3]")
        List<Integer> idsResumo
) {}


