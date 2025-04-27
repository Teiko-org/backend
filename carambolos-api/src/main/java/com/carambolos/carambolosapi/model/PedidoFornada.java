package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Entity
@Table(name = "pedido_fornada")
@Schema(description = "Entidade que representa um pedido associado a uma fornada.")
public class PedidoFornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do pedido da fornada", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fornada_da_vez_id")
    @NotNull
    @Schema(description = "Fornada da vez relacionada ao pedido", implementation = FornadaDaVez.class)
    private FornadaDaVez fornadaDaVez;

    @OneToOne
    @JoinColumn(name = "endereco_id")
    @NotNull
    @Schema(description = "Endereço de entrega do pedido", implementation = Endereco.class)
    private Endereco endereco;

    @OneToOne(optional = true)
    @JoinColumn(name = "usuario_id")
    @NotNull
    @Schema(description = "Usuário que realizou o pedido", implementation = Usuario.class)
    private Usuario usuario;

    @NotNull
    @Schema(description = "Quantidade de itens no pedido", example = "10")
    private Integer quantidade;

    @Column(name = "data_previsao_entrega")
    @NotNull
    @Schema(description = "Data prevista para entrega do pedido", example = "2025-05-10")
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
