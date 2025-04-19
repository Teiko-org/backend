package com.carambolos.carambolosapi.model.projection;

public class RecheioPedidoProjection {
    private Integer id;
    private String sabor1;
    private String sabor2;
    private Double valor;
    private int isAtivo;

    public RecheioPedidoProjection(Integer id, String sabor1, String sabor2, Double valor, int isAtivo) {
        this.id = id;
        this.sabor1 = sabor1;
        this.sabor2 = sabor2;
        this.valor = valor;
        this.isAtivo = isAtivo;
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

    public void setSabor1(String sabor1) {
        this.sabor1 = sabor1;
    }

    public String getSabor2() {
        return sabor2;
    }

    public void setSabor2(String sabor2) {
        this.sabor2 = sabor2;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(int isAtivo) {
        this.isAtivo = isAtivo;
    }
}
