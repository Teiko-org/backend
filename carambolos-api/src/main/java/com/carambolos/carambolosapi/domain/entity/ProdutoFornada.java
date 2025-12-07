package com.carambolos.carambolosapi.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProdutoFornada implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String produto;
    private String descricao;
    private List<ImagemProdutoFornada> imagens = new ArrayList<>();
    private Double valor;
    private String categoria;
    private Boolean isAtivo = true;

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }

    public List<ImagemProdutoFornada> getImagens() {
        return imagens;
    }

    public void setImagens(List<ImagemProdutoFornada> imagens) {
        this.imagens = imagens;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(boolean isAtivo) {
        this.isAtivo = isAtivo;
    }
}
