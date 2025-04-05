package com.carambolos.carambolosapi.controller.projection;

import java.util.UUID;

public interface FornadaDaVezProjection {
    UUID getId();
    UUID getProdutoFornadaId();
    UUID getFornadaId();
    Integer getQuantidade();
}
