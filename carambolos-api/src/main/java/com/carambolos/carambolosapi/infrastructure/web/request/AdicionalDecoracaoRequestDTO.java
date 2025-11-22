package com.carambolos.carambolosapi.infrastructure.web.request;

import java.util.List;

public record AdicionalDecoracaoRequestDTO(
        Integer decoracaoId,
        List<Integer> adicionalId
) {
}
