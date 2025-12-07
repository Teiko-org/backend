package com.carambolos.carambolosapi.infrastructure.web.response;

public record AdicionalResponseDTO (
        Integer id,
        String descricao,
        Boolean isAtivo
) {
}
