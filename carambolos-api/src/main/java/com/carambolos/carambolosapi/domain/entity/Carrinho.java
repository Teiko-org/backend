package com.carambolos.carambolosapi.domain.entity;

import java.time.LocalDateTime;

public class Carrinho {
    private Integer id;
    private Integer usuarioId;
    private String itens;
    private LocalDateTime dataUltimaAtualizacao;

    public Carrinho() {
    }

    public Carrinho(Integer id, Integer usuarioId, String itens, LocalDateTime dataUltimaAtualizacao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.itens = itens;
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getItens() {
        return itens;
    }

    public void setItens(String itens) {
        this.itens = itens;
    }

    public LocalDateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }
}