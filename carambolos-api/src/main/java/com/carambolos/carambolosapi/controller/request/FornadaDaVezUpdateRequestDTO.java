package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.FornadaDaVez;

public record FornadaDaVezUpdateRequestDTO(
        Integer quantidade
) {
    public FornadaDaVez toEntity() {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setQuantidade(quantidade);
        return fornadaDaVez;
    }
}
