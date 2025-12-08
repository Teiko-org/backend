package com.carambolos.carambolosapi.domain.entity;

public class AdicionalItem {
    private Integer id;
    private String descricao;

    public AdicionalItem() {
    }

    public AdicionalItem(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
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
}
