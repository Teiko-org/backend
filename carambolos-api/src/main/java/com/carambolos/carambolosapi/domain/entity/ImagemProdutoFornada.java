package com.carambolos.carambolosapi.domain.entity;

import java.io.Serializable;

public class ImagemProdutoFornada implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String url;
    private ProdutoFornada produtoFornada;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProdutoFornada getProdutoFornada() {
        return produtoFornada;
    }

    public void setProdutoFornada(ProdutoFornada produtoFornada) {
        this.produtoFornada = produtoFornada;
    }
}
