package com.carambolos.carambolosapi.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

public class RecheioExclusivo {
    private Integer id;
    private Integer recheioUnitarioId1;
    private Integer recheioUnitarioId2;
    private String nome;
    private Boolean isAtivo = true;

    public RecheioExclusivo() {
    }

    public RecheioExclusivo(Integer id, Integer recheioUnitarioId1, Integer recheioUnitarioId2, String nome, Boolean isAtivo) {
        this.id = id;
        this.recheioUnitarioId1 = recheioUnitarioId1;
        this.recheioUnitarioId2 = recheioUnitarioId2;
        this.nome = nome;
        this.isAtivo = isAtivo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecheioUnitarioId1() {
        return recheioUnitarioId1;
    }

    public void setRecheioUnitarioId1(Integer recheioUnitarioId1) {
        this.recheioUnitarioId1 = recheioUnitarioId1;
    }

    public Integer getRecheioUnitarioId2() {
        return recheioUnitarioId2;
    }

    public void setRecheioUnitarioId2(Integer recheioUnitarioId2) {
        this.recheioUnitarioId2 = recheioUnitarioId2;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
