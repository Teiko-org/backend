package com.carambolos.carambolosapi.infrastructure.web.request;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.MassaEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para requisições de criação ou atualização de Massa")
public class MassaRequestDTO {
    @NotBlank
    @Schema(description = "Sabor da massa", example = "Baunilha")
    private String sabor;

    @DecimalMin("0.0")
    @Schema(description = "Valor da massa", example = "12.50")
    private Double valor;

    public @NotBlank String getSabor() {
        return sabor;
    }

    public void setSabor(@NotBlank String sabor) {
        this.sabor = sabor;
    }

    public @DecimalMin("0.0") Double getValor() {
        return valor;
    }

    public void setValor(@DecimalMin("0.0") Double valor) {
        this.valor = valor;
    }
}
