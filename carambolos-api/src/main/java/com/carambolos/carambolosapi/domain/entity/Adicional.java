package com.carambolos.carambolosapi.domain.entity;

public class Adicional {
    private Integer id;
    private String descricao;
    private Boolean isAtivo = true;

    public Adicional(Integer id, String descricao, Boolean isAtivo) {
        this.id = id;
        this.descricao = descricao;
        this.isAtivo = isAtivo;
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
