package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class PedidoFornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fornada_da_vez_id")
    private FornadaDaVez fornadaDaVez;

    @OneToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @OneToOne(optional = true)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private Integer quantidade;
    private Date data_previsao_entrega;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FornadaDaVez getFornadaDaVez() {
        return fornadaDaVez;
    }

    public void setFornadaDaVez(FornadaDaVez fornadaDaVez) {
        this.fornadaDaVez = fornadaDaVez;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Date getData_previsao_entrega() {
        return data_previsao_entrega;
    }

    public void setData_previsao_entrega(Date data_previsao_entrega) {
        this.data_previsao_entrega = data_previsao_entrega;
    }
}
