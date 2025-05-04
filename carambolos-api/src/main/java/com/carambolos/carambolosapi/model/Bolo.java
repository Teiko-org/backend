package com.carambolos.carambolosapi.model;

import com.carambolos.carambolosapi.model.enums.FormatoEnum;
import com.carambolos.carambolosapi.model.enums.TamanhoEnum;
import jakarta.persistence.*;

@Entity
public class Bolo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "recheio_pedido_id")
    private Integer recheioPedido;
    @Column(name = "massa_id")
    private Integer massa;
    @Column(name = "cobertura_id")
    private Integer cobertura;
//    @Column(name = "decoracao_id")
//    private Integer decoracao;
    @Enumerated
    private FormatoEnum formato;
    @Enumerated
    private TamanhoEnum tamanho;
    @Column(name = "is_ativo")
    private Boolean isAtivo = true;

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
