package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "pedido_fornada")
public class PedidoFornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fornada_da_vez_id")
    @NotNull
    private FornadaDaVez fornadaDaVez;

    @OneToOne
    @JoinColumn(name = "endereco_id")
    @NotNull
    private Endereco endereco;

    @OneToOne(optional = true)
    @JoinColumn(name = "usuario_id")
    @NotNull
    private Usuario usuario;

    @NotNull
    private Integer quantidade;

    @Column(name = "data_previsao_entrega")
    @NotNull
    private LocalDate dataPrevisaoEntrega;

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

    public LocalDate getDataPrevisaoEntrega() {
        return dataPrevisaoEntrega;
    }

    public void setDataPrevisaoEntrega(LocalDate dataPrevisaoEntrega) {
        this.dataPrevisaoEntrega = dataPrevisaoEntrega;
    }
}
