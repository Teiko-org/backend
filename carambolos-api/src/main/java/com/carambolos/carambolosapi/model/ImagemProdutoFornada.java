package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "imagem_produto_fornada")
public class ImagemProdutoFornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500, nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "produto_fornada_id", nullable = false)
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
