package com.carambolos.carambolosapi.infrastructure.persistence.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Schema(description = "Entidade que representa uma cobertura")
public class CoberturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da cobertura", example = "1")
    private Integer id;

    @Schema(description = "Cor da cobertura", example = "Azul")
    private String cor;

    @Schema(description = "Descrição da cobertura", example = "Cobertura resistente a água")
    private String descricao;

    @Column(name = "is_ativo")
    @Schema(description = "Indica se a cobertura está ativa", example = "true")
    private Boolean isAtivo = true;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
