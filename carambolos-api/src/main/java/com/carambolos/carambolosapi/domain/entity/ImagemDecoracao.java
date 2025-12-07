package com.carambolos.carambolosapi.domain.entity;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.DecoracaoEntity;

public class ImagemDecoracao {
    private Integer id;
    private String url;
    private Decoracao decoracao;

    public ImagemDecoracao() {
    }

    public ImagemDecoracao(Integer id, String url, Decoracao decoracao) {
        this.id = id;
        this.url = url;
        this.decoracao = decoracao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        if (url == null) {

            return "";

        }

        return url;

    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Decoracao getDecoracao() {
        return decoracao;
    }

    public void setDecoracao(Decoracao decoracaoEntity) {
        this.decoracao = decoracaoEntity;
    }
}
