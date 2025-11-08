package com.carambolos.carambolosapi.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "carrinho")
public class CarrinhoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id", nullable = false, unique = true)
    private Integer usuarioId;

    @Column(name = "itens", columnDefinition = "TEXT")
    private String itens;

    @Column(name = "data_ultima_atualizacao")
    private LocalDateTime dataUltimaAtualizacao;

    public CarrinhoEntity() {
    }

    public CarrinhoEntity(Integer id, Integer usuarioId, String itens, LocalDateTime dataUltimaAtualizacao) {
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