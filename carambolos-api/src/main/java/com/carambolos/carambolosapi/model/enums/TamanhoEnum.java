package com.carambolos.carambolosapi.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação dos tamanhos disponíveis para o bolo.")
public enum TamanhoEnum {
    TAMANHO_5,
    TAMANHO_7,
    TAMANHO_12,
    TAMANHO_15,
    TAMANHO_17
}
