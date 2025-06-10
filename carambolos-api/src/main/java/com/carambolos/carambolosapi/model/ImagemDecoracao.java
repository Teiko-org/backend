package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "imagem_decoracao")
public class ImagemDecoracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500, nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decoracao_id", nullable = false)
    private Decoracao decoracao;

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

    public void setDecoracao(Decoracao decoracao) {
        this.decoracao = decoracao;
    }
}
