package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Fornada;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO para criar uma nova fornada")
public record FornadaRequestDTO(

        @Schema(description = "ID da fornada", example = "1")
        Integer id,

        @Schema(description = "Data de início da fornada", example = "2025-05-01")
        LocalDate dataInicio,

        @Schema(description = "Data de fim da fornada", example = "2025-05-07")
        LocalDate dataFim

) {

    @Schema(description = "Método para converter o DTO em uma entidade Fornada")
    public Fornada toEntity() {
        Fornada fornada = new Fornada();
        fornada.setDataInicio(dataInicio);
        fornada.setDataFim(dataFim);
        return fornada;
    }
}
