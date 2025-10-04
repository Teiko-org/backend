package com.carambolos.carambolosapi.domain.entity;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class Fornada {
    private Integer id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Boolean isAtivo = true;

    public Fornada(LocalDate dataInicio, LocalDate dataFim, Boolean isAtivo) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Data de início e data de fim não podem ser nulas.");
        }
        if (dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("Data de fim não pode ser anterior à data de início.");
        }
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.isAtivo = isAtivo;
    }

    public void desativar() {
        this.isAtivo = false;
    }

    public void ativar() {
        this.isAtivo = true;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
