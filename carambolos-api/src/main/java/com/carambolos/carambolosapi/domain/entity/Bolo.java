package com.carambolos.carambolosapi.domain.entity;

import com.carambolos.carambolosapi.domain.enums.FormatoEnum;
import com.carambolos.carambolosapi.domain.enums.TamanhoEnum;

import java.io.Serializable;

public class Bolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer recheioPedido;
    private Integer massa;
    private Integer cobertura;
    private Integer decoracao;
    private FormatoEnum formato;
    private TamanhoEnum tamanho;
    private String categoria;
    private Boolean isAtivo = true;

    public Bolo() {
    }

    public Bolo(Integer id, Integer recheioPedido, Integer massa, Integer cobertura, Integer decoracao, FormatoEnum formato, TamanhoEnum tamanho, String categoria, Boolean isAtivo) {
        this.id = id;
        this.recheioPedido = recheioPedido;
        this.massa = massa;
        this.cobertura = cobertura;
        this.decoracao = decoracao;
        this.formato = formato;
        this.tamanho = tamanho;
        this.categoria = categoria;
        this.isAtivo = isAtivo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecheioPedido() {
        return recheioPedido;
    }

    public void setRecheioPedido(Integer recheioPedido) {
        this.recheioPedido = recheioPedido;
    }

    public Integer getMassa() {
        return massa;
    }

    public void setMassa(Integer massa) {
        this.massa = massa;
    }

    public Integer getCobertura() {
        return cobertura;
    }

    public void setCobertura(Integer cobertura) {
        this.cobertura = cobertura;
    }

    public Integer getDecoracao() {
        return decoracao;
    }

    public void setDecoracao(Integer decoracao) {
        this.decoracao = decoracao;
    }

    public FormatoEnum getFormato() {
        return formato;
    }

    public void setFormato(FormatoEnum formato) {
        this.formato = formato;
    }

    public TamanhoEnum getTamanho() {
        return tamanho;
    }

    public void setTamanho(TamanhoEnum tamanho) {
        this.tamanho = tamanho;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
