package com.carambolos.carambolosapi.model.projection;

import java.sql.Date;
import java.time.LocalDate;

public class ProdutoFornadaDaVezProjection {
    Integer fornadaDaVezId;
    Integer produtoFornadaId;
    String produto;
    String descricao;
    Double valor;
    String categoria;
    Integer quantidade;
    Boolean isAtivoPf;
    Boolean isAtivoFdv;
    LocalDate dataInicio;
    LocalDate dataFim;

    public ProdutoFornadaDaVezProjection(Integer fornadaDaVezId, Integer produtoFornadaId, String produto, String descricao, Double valor, String categoria, Integer quantidade, int isAtivoPf, int isAtivoFdv, Date dataInicio, Date dataFim) {
        this.fornadaDaVezId = fornadaDaVezId;
        this.produtoFornadaId = produtoFornadaId;
        this.produto = produto;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.quantidade = quantidade;
        if (isAtivoPf == 1) {
            this.isAtivoPf = true;
        } else {
            this.isAtivoPf = false;
        }
        if (isAtivoFdv == 1) {
            this.isAtivoFdv = true;
        } else {
            this.isAtivoFdv = false;
        }
        this.dataInicio = dataInicio.toLocalDate();
        this.dataFim = dataFim.toLocalDate();
    }

    public Integer getFornadaDaVezId() {
        return fornadaDaVezId;
    }

    public void setFornadaDaVezId(Integer fornadaDaVezId) {
        this.fornadaDaVezId = fornadaDaVezId;
    }

    public Integer getProdutoFornadaId() {
        return produtoFornadaId;
    }

    public void setProdutoFornadaId(Integer produtoFornadaId) {
        this.produtoFornadaId = produtoFornadaId;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Boolean getAtivoPf() {
        return isAtivoPf;
    }

    public void setAtivoPf(Boolean ativoPf) {
        isAtivoPf = ativoPf;
    }

    public Boolean getAtivoFdv() {
        return isAtivoFdv;
    }

    public void setAtivoFdv(Boolean ativoFdv) {
        isAtivoFdv = ativoFdv;
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
}
