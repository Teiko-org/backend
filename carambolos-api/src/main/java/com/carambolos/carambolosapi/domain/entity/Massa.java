package com.carambolos.carambolosapi.domain.entity;

import java.io.Serializable;

public class Massa implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String sabor;
    private Double valor;
    private Boolean isAtivo = true;

    public Massa(Integer id, String sabor, Double valor, Boolean isAtivo) {
        this.id = id;
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
