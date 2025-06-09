package com.carambolos.carambolosapi.model.projection;

public class DetalheBoloProjection {
    Integer boloId;
    String produto;
    String categoria;
    String saborMassa;
    String saborRecheio;
    String corCobertura;
    String formato;
    String tamanho;
    Double precoTotal;
    Boolean isAtivo;

    public DetalheBoloProjection(Integer boloId, String produto, String categoria, String saborMassa, String saborRecheio, String corCobertura, String formato, String tamanho, Double precoTotal, int isAtivo) {
        this.boloId = boloId;
        this.produto = produto;
        this.categoria = categoria;
        this.saborMassa = saborMassa;
        this.saborRecheio = saborRecheio;
        this.corCobertura = corCobertura;
        this.formato = formato;
        this.tamanho = tamanho;
        this.precoTotal = precoTotal;

        if (isAtivo == 0) {
            this.isAtivo = false;
        } else {
            this.isAtivo = true;
        }
    }

    public Integer getBoloId() {
        return boloId;
    }

    public void setBoloId(Integer boloId) {
        this.boloId = boloId;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSaborMassa() {
        return saborMassa;
    }

    public void setSaborMassa(String saborMassa) {
        this.saborMassa = saborMassa;
    }

    public String getSaborRecheio() {
        return saborRecheio;
    }

    public void setSaborRecheio(String saborRecheio) {
        this.saborRecheio = saborRecheio;
    }

    public String getCorCobertura() {
        return corCobertura;
    }

    public void setCorCobertura(String corCobertura) {
        this.corCobertura = corCobertura;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public Double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(Double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
