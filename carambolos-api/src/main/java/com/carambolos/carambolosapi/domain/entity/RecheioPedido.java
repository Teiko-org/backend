package com.carambolos.carambolosapi.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;


@Entity(name = "recheio_pedido")
@Schema(description = "Entidade que representa o recheio de um pedido")
public class RecheioPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do recheio do pedido", example = "1")
    private Integer id;

    @Column(name = "recheio_unitario_id1")
    @Schema(description = "ID do primeiro recheio unitário relacionado ao pedido", example = "10")
    private Integer recheioUnitarioId1;

    @Column(name = "recheio_unitario_id2")
    @Schema(description = "ID do segundo recheio unitário relacionado ao pedido", example = "11")
    private Integer recheioUnitarioId2;

    @Column(name = "recheio_exclusivo")
    @Schema(description = "ID do recheio exclusivo relacionado ao pedido", example = "5")
    private Integer recheioExclusivo;

    @Column(name = "is_ativo")
    @Schema(description = "Indica se o recheio do pedido está ativo", example = "true")
    private Boolean isAtivo = true;

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

    public Integer getRecheioExclusivo() {
        return recheioExclusivo;
    }

    public void setRecheioExclusivo(Integer recheioExclusivo) {
        this.recheioExclusivo = recheioExclusivo;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
