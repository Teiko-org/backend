package com.carambolos.carambolosapi.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;


@Entity
@Schema(description = "Entidade que representa uma massa")
public class Massa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da massa", example = "1")
    public Integer id;

    @Schema(description = "Sabor da massa", example = "Chocolate")
    public String sabor;

    @Schema(description = "Valor da massa", example = "15.50")
    public Double valor;

    @Column(name = "is_ativo")
    @Schema(description = "Indica se a massa está ativa", example = "true")
    public Boolean isAtivo = true;

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
