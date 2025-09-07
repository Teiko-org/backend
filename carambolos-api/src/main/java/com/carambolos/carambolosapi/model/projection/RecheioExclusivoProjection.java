package com.carambolos.carambolosapi.model.projection;

public class RecheioExclusivoProjection {
    private Integer id;
    private String nome;
    private String sabor1;
    private String sabor2;
    private Integer isAtivo;

    // Aceita tipos genéricos vindos de consultas nativas (Long/BigInteger/String)
    public RecheioExclusivoProjection(Object id, String nome, String sabor1, String sabor2, Object isAtivo) {
        this.id = convertToInteger(id);
        this.nome = nome;
        this.sabor1 = sabor1;
        this.sabor2 = sabor2;
        this.isAtivo = convertToInteger(isAtivo);
    }

    private Integer convertToInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        try { return Integer.parseInt(String.valueOf(value)); } catch (Exception e) { return null; }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Integer getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Integer isAtivo) {
        this.isAtivo = isAtivo;
    }
}
