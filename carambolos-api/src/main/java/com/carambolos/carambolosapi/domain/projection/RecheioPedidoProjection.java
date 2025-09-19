package com.carambolos.carambolosapi.domain.projection;

public class RecheioPedidoProjection {
    private Integer id;
    private String sabor1;
    private String sabor2;
    private Double valor;
    private Boolean isAtivo;

    public RecheioPedidoProjection(Integer id, String sabor1, String sabor2, Double valor, Boolean isAtivo) {
        this.id = id;
        this.sabor1 = sabor1;
        this.sabor2 = sabor2;
        this.valor = valor;
        this.isAtivo = Boolean.TRUE.equals(isAtivo);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSabor1() {
        return sabor1;
    }

    public void setSabor1(String sabor1) { this.sabor1 = sabor1; }

    public String getSabor2() {
        return sabor2;
    }

    public void setSabor2(String sabor2) { this.sabor2 = sabor2; }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Boolean getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Boolean isAtivo) {
        this.isAtivo = isAtivo;
    }
}
