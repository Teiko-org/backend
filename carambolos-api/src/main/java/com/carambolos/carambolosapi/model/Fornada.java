package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Entity
@Schema(description = "Entidade que representa uma fornada, contendo data de início e fim da produção.")
public class Fornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da fornada", example = "1")
    private Integer id;

    @Column(name = "data_inicio")
    @NotNull
    @Schema(description = "Data de início da fornada", example = "2025-05-01")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    @NotNull
    @Schema(description = "Data de fim da fornada", example = "2025-05-07")
    private LocalDate dataFim;

    @Column(name = "is_ativo")
    @Schema(description = "Indica se a fornada está ativa ou não", example = "true")
    private Boolean isAtivo = true;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
