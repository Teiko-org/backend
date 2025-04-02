package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Fornada {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Date data_inicio;
    private Date data_fim;

    public UUID getId() {
        return id = UUID.randomUUID();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(Date data_inicio) {
        this.data_inicio = data_inicio;
    }

    public Date getData_fim() {
        return data_fim;
    }

    public void setData_fim(Date data_fim) {
        this.data_fim = data_fim;
    }
}
