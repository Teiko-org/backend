package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.enums.FormatoEnum;
import com.carambolos.carambolosapi.domain.enums.TamanhoEnum;

public record BoloCompletoResponseDTO(
        Integer id,
        RecheioPedidoResponseDTO recheioPedido,
        MassaResponseDTO massa,
        CoberturaResponseDTO cobertura,
        DecoracaoResponseDTO decoracao,
        FormatoEnum formato,
        TamanhoEnum tamanho
) {
}
