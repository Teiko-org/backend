package com.carambolos.carambolosapi.model;

import com.carambolos.carambolosapi.model.enums.FormatoEnum;
import com.carambolos.carambolosapi.model.enums.TamanhoEnum;
import jakarta.persistence.*;

@Entity
public class Bolo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private RecheioPedido recheioPedido;
    private Massa massa;
    public Cobertura cobertura;
    //TODO - add decoracao
    @Enumerated
    public FormatoEnum formato;
    @Enumerated
    public TamanhoEnum tamanho;
    @Column(name = "is_ativo")
    public Boolean isAtivo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RecheioPedido getRecheioPedido() {
        return recheioPedido;
    }

    public void setRecheioPedido(RecheioPedido recheioPedido) {
        this.recheioPedido = recheioPedido;
    }

    public Massa getMassa() {
        return massa;
    }

    public void setMassa(Massa massa) {
        this.massa = massa;
    }

    public Cobertura getCobertura() {
        return cobertura;
    }

    public void setCobertura(Cobertura cobertura) {
        this.cobertura = cobertura;
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

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
