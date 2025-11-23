package com.carambolos.carambolosapi.domain.entity;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.ImagemDecoracaoEntity;

import java.util.ArrayList;
import java.util.List;

public class Decoracao {
    private Integer id;
    private List<ImagemDecoracao> imagens = new ArrayList<>();
    private String observacao;
    private String nome;
    private Boolean isAtivo = true;
    private List<Integer> adicionaisIds = new ArrayList<>();
    private String categoria;

    public Decoracao() {
    }

    public Decoracao(Integer id, List<ImagemDecoracao> imagens, String observacao, String nome, Boolean isAtivo, List<Integer> adicionaisIds, String categoria) {
        this.id = id;
        this.imagens = imagens;
        this.observacao = observacao;
        this.nome = nome;
        this.isAtivo = isAtivo;
        this.adicionaisIds = adicionaisIds;
        this.categoria = categoria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ImagemDecoracao> getImagens() {
        return imagens;
    }

    public void setImagens(List<ImagemDecoracao> imagens) {
        this.imagens = imagens;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }

    public Boolean getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Boolean isAtivo) {
        this.isAtivo = isAtivo;
    }

    public List<Integer> getAdicionaisIds() {
        return adicionaisIds;
    }

    public void setAdicionaisIds(List<Integer> adicionaisIds) {
        this.adicionaisIds = adicionaisIds;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
