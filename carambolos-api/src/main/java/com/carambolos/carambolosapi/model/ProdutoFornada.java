package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ProdutoFornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String produto;
    private String descricao;
    private Double valor;

    @OneToMany(mappedBy = "produtoFornada")
    private List<FornadaDaVez> fornadasDaVez;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
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

    public List<FornadaDaVez> getFornadasDaVez() {
        return fornadasDaVez;
    }

    public void setFornadasDaVez(List<FornadaDaVez> fornadasDaVez) {
        this.fornadasDaVez = fornadasDaVez;
    }
}

