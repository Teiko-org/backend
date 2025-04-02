package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class ProdutoFornada {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String produto;
    private String descricao;
    private Double valor;

    public UUID getId() {
        return id = UUID.randomUUID();
    }

    public void setId(UUID id) {
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
}

