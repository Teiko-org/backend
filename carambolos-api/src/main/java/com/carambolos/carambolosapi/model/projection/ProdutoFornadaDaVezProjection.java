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

    // Construtor único que aceita Object para lidar com diferentes tipos do MySQL
    public ProdutoFornadaDaVezProjection(Integer fornadaDaVezId, Integer produtoFornadaId, String produto, String descricao, Double valor, String categoria, Integer quantidade, Object isAtivoPf, Object isAtivoFdv, Date dataInicio, Date dataFim) {
        this.fornadaDaVezId = fornadaDaVezId;
        this.produtoFornadaId = produtoFornadaId;
        this.produto = produto;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.quantidade = quantidade;
        
        // Converter Object para Boolean de forma segura
        this.isAtivoPf = convertToBoolean(isAtivoPf);
        this.isAtivoFdv = convertToBoolean(isAtivoFdv);
        
        this.dataInicio = (dataInicio != null ? dataInicio.toLocalDate() : null);
        this.dataFim = (dataFim != null ? dataFim.toLocalDate() : null);
    }
    
    // Método auxiliar para converter Object para Boolean
    private Boolean convertToBoolean(Object value) {
        if (value == null) {
            return false;
        }
        
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        
        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }
        
        if (value instanceof String) {
            return "1".equals(value) || "true".equalsIgnoreCase((String) value);
        }
        
        return false;
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
