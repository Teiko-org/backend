package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Fornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date data_inicio;
    private Date data_fim;

    @OneToMany(mappedBy = "fornada")
    private List<FornadaDaVez> fornadasDaVez;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<FornadaDaVez> getFornadasDaVez() {
        return fornadasDaVez;
    }

    public void setFornadasDaVez(List<FornadaDaVez> fornadasDaVez) {
        this.fornadasDaVez = fornadasDaVez;
    }
}
