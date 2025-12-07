package com.carambolos.carambolosapi.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "adicional")
public class AdicionalEntity {
    @Id
    private Integer id;
    private String descricao;
    @Column(name = "is_ativo")
    private Boolean isAtivo = true;

    public AdicionalEntity() {
    }

    public AdicionalEntity(Integer id, String descricao, int isAtivo) {
        this.id = id;
        this.descricao = descricao;

        this.isAtivo = isAtivo == 1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
