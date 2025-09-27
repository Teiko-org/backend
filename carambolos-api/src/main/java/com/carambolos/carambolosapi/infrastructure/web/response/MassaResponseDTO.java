package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.MassaEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de resposta para dados da Massa")
public class MassaResponseDTO {
    @Schema(description = "Identificador único da massa", example = "1")
    private Integer id;

    @Schema(description = "Sabor da massa", example = "Chocolate")
    private String sabor;

    @Schema(description = "Valor da massa", example = "15.90")
    private Double valor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
