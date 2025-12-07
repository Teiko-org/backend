package com.carambolos.carambolosapi.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "imagem_decoracao")
public class ImagemDecoracaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500, nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decoracao_id", nullable = false)
    private DecoracaoEntity decoracao;

    public ImagemDecoracaoEntity() {
    }

    public ImagemDecoracaoEntity(Integer id, String url, DecoracaoEntity decoracao) {
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

    public DecoracaoEntity getDecoracao() {
        return decoracao;
    }

    public void setDecoracao(DecoracaoEntity decoracaoEntity) {
        this.decoracao = decoracaoEntity;
    }
}
