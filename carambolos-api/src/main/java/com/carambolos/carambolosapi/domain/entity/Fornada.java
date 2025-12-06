package com.carambolos.carambolosapi.domain.entity;

import java.io.Serializable;
import java.time.LocalDate;

public class Fornada implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Boolean isAtivo = true;

    // Construtor padrão para uso em mapeamentos (sem validação)
    public Fornada() {
    }

    // Construtor com validação para criação de novas fornadas pela aplicação
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
