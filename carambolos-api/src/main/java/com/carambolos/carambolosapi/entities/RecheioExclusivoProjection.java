package com.carambolos.carambolosapi.entities;

import com.carambolos.carambolosapi.model.RecheioUnitario;

public class RecheioExclusivoProjection {
    private Integer id;
    private String nome;
    private String sabor1;
    private String sabor2;

    public RecheioExclusivoProjection(Integer id, String nome, String sabor1, String sabor2) {
        this.id = id;
        this.nome = nome;
        this.sabor1 = sabor1;
        this.sabor2 = sabor2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSabor1() {
        return sabor1;
    }

    public void setSabor1(String sabor1) {
        this.sabor1 = sabor1;
    }

    public String getSabor2() {
        return sabor2;
    }

    public void setSabor2(String sabor2) {
        this.sabor2 = sabor2;
    }
}
