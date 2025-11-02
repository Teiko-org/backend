package com.carambolos.carambolosapi.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "decoracao")
@Schema(description = "Entidade que representa a decoração de um bolo")
public class Decoracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da decoração", example = "1")
    private Integer id;

    @OneToMany(mappedBy = "decoracao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagemDecoracao> imagens = new ArrayList<>();

    @Schema(description = "Observação adicional sobre a decoração", example = "Tema do Homem-Aranha com cores azul e vermelha")
    private String observacao;

    @Schema(description = "Nome do tipo da decoração", example = "Bolo de natal")
    private String nome;

    @Column(name = "is_ativo")
    @Schema(description = "Indica se a decoração está ativa", example = "true")
    private Boolean isAtivo = true;

    @Schema(description = "Categoria para exibição (pré-decoração)", example = "Vintage")
    private String categoria;

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
