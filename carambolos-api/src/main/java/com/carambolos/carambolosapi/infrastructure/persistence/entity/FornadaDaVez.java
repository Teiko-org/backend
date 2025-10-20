package com.carambolos.carambolosapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "fornada_da_vez")
@Schema(description = "Entidade que representa a produção de um produto específico em uma fornada.")
public class FornadaDaVez {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "produto_fornada_id")
    private Integer produtoFornada;

    @Column(name = "fornada_id")
    private Integer fornada;

    private Integer quantidade;

    @Column(name = "is_ativo")
    private Boolean isAtivo = true;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProdutoFornada() {
        return produtoFornada;
    }

    public void setProdutoFornada(Integer produtoFornada) {
        this.produtoFornada = produtoFornada;
    }

    public Integer getFornada() {
        return fornada;
    }

    public void setFornada(Integer fornada) {
        this.fornada = fornada;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
