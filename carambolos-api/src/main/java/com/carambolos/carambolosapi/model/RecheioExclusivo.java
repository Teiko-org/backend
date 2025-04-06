package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;

@Entity(name = "recheio_exclusivo")
public class RecheioExclusivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "recheio_unitario_id1")
    private Integer recheioUnitarioId1;
    @Column(name = "recheio_unitario_id2")
    private Integer recheioUnitarioId2;
    private String nome;

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
}
