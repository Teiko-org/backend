package com.carambolos.carambolosapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
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

    @Column(name = "is_ativo")
    @Schema(description = "Indica se a decoração está ativa", example = "true")
    private Boolean isAtivo = true;

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

    public Boolean getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Boolean isAtivo) {
        this.isAtivo = isAtivo;
    }
}
