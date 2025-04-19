package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Currency;

@Entity(name = "recheio_pedido")
public class RecheioPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "recheio_unitario_id1")
    private Integer recheioUnitarioId1;
    @Column(name = "recheio_unitario_id2")
    private Integer recheioUnitarioId2;
    @Column(name = "recheio_exclusivo")
    private Integer recheioExclusivo;
    @Column(name = "is_ativo")
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
