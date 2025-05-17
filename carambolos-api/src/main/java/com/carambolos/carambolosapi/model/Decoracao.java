package com.carambolos.carambolosapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Schema(description = "Entidade que representa a decoração de um bolo")
public class Decoracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da decoração", example = "1")
    private Integer id;

    @Column(name = "imagem_referencia", length = 500)
    @Schema(description = "URL da imagem de referência da decoração", example = "https://storage.azure.com/decoracoes/bolo123.jpg")
    private String imagemReferencia;

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

    public String getImagemReferencia() {
        return imagemReferencia;
    }

    public void setImagemReferencia(String imagemReferencia) {
        this.imagemReferencia = imagemReferencia;
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
