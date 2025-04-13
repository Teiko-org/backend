package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.FornadaDaVez;

public record FornadaDaVezUpdateRequest(
        Integer quantidade
) {
    public FornadaDaVez toEntity() {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setQuantidade(quantidade);
        return fornadaDaVez;
    }
}
