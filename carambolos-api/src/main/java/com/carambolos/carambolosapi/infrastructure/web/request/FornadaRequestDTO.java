package com.carambolos.carambolosapi.infrastructure.web.request;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.Fornada;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO para criar uma nova fornada")
public record FornadaRequestDTO(

        @Schema(description = "ID da fornada", example = "1")
        Integer id,

        @Schema(description = "Data de início da fornada", example = "2025-05-01")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataInicio,

        @Schema(description = "Data de fim da fornada", example = "2025-05-07")
        @JsonFormat(pattern = "yyyy-MM-dd")
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
