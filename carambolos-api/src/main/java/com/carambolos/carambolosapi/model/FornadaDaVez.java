package com.carambolos.carambolosapi.model;
import jakarta.persistence.*;

@Entity
@Table(name = "fornada_da_vez")
public class FornadaDaVez {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "produto_fornada_id", nullable = false)
    private ProdutoFornada produtoFornada;

    @ManyToOne
    @JoinColumn(name = "fornada_id")
    private Fornada fornada;

    private Integer quantidade;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProdutoFornada getProdutoFornada() {
        return produtoFornada;
    }

    public void setProdutoFornada(ProdutoFornada produtoFornada) {
        this.produtoFornada = produtoFornada;
    }

    public Fornada getFornada() {
        return fornada;
    }

    public void setFornada(Fornada fornada) {
        this.fornada = fornada;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
