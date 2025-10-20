package com.carambolos.carambolosapi.infrastructure.persistence.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity(name = "recheio_exclusivo")
@Schema(description = "Entidade que representa um recheio exclusivo")
public class RecheioExclusivoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do recheio exclusivo", example = "1")
    private Integer id;

    @Column(name = "recheio_unitario_id1")
    @Schema(description = "ID do primeiro recheio unitário relacionado", example = "10")
    private Integer recheioUnitarioId1;

    @Column(name = "recheio_unitario_id2")
    @Schema(description = "ID do segundo recheio unitário relacionado", example = "11")
    private Integer recheioUnitarioId2;

    @Schema(description = "Nome do recheio exclusivo", example = "Dois Amores")
    private String nome;

    @Column(name = "is_ativo")
    @Schema(description = "Indica se o recheio exclusivo está ativo", example = "true")
    private Boolean isAtivo = true;

    public RecheioExclusivoEntity() {
    }

    public RecheioExclusivoEntity(Integer id, Integer recheioUnitarioId1, Integer recheioUnitarioId2, String nome, Boolean isAtivo) {
        this.id = id;
        this.recheioUnitarioId1 = recheioUnitarioId1;
        this.recheioUnitarioId2 = recheioUnitarioId2;
        this.nome = nome;
        this.isAtivo = isAtivo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecheioUnitarioId1() {
        return recheioUnitarioId1;
    }

    public void setRecheioUnitarioId1(Integer recheioUnitarioId1) {
        this.recheioUnitarioId1 = recheioUnitarioId1;
    }

    public Integer getRecheioUnitarioId2() {
        return recheioUnitarioId2;
    }

    public void setRecheioUnitarioId2(Integer recheioUnitarioId2) {
        this.recheioUnitarioId2 = recheioUnitarioId2;
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
}
