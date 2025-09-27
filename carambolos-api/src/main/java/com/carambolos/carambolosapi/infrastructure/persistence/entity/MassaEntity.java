package com.carambolos.carambolosapi.infrastructure.persistence.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;


@Entity
@Schema(description = "Entidade que representa uma massa")
public class MassaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da massa", example = "1")
    private Integer id;

    @Schema(description = "Sabor da massa", example = "Chocolate")
    private String sabor;

    @Schema(description = "Valor da massa", example = "15.50")
    private Double valor;

    @Column(name = "is_ativo")
    @Schema(description = "Indica se a massa está ativa", example = "true")
    private Boolean isAtivo = true;

    public MassaEntity() {
    }

    public MassaEntity(String sabor, Double valor, Boolean isAtivo) {
        this.sabor = sabor;
        this.valor = valor;
        this.isAtivo = isAtivo;
    }

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

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
