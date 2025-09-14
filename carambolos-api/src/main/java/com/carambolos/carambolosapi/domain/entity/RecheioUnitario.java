package com.carambolos.carambolosapi.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity(name = "recheio_unitario")
@Schema(description = "Entidade que representa um recheio unitário")
public class RecheioUnitario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do recheio unitário", example = "1")
    private Integer id;

    @Schema(description = "Sabor do recheio unitário", example = "Chocolate")
    private String sabor;

    @Schema(description = "Descrição do recheio unitário", example = "Recheio cremoso de chocolate meio amargo")
    private String descricao;

    @Schema(description = "Valor do recheio unitário", example = "5.50")
    private Double valor;

    @Column(name = "is_ativo")
    @Schema(description = "Indica se o recheio unitário está ativo", example = "true")
    private Boolean isAtivo = true;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
