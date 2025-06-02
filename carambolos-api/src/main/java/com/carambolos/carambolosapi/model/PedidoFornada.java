package com.carambolos.carambolosapi.model;

import com.carambolos.carambolosapi.model.enums.TipoEntregaEnum;
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
    private Integer id;

    @Column(name = "fornada_da_vez_id")
    private Integer fornadaDaVez;

    @Column(name = "endereco_id")
    private Integer endereco;

    @Column(name = "usuario_id")
    private Integer usuario;

    private Integer quantidade;

    @Column(name = "data_previsao_entrega")
    private LocalDate dataPrevisaoEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_entrega")
    private TipoEntregaEnum tipoEntrega;

    @Column(name = "is_ativo")
    private Boolean isAtivo = true;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFornadaDaVez() {
        return fornadaDaVez;
    }

    public void setFornadaDaVez(Integer fornadaDaVez) {
        this.fornadaDaVez = fornadaDaVez;
    }

    public Integer getEndereco() {
        return endereco;
    }

    public void setEndereco(Integer endereco) {
        this.endereco = endereco;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
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

    public TipoEntregaEnum getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntregaEnum tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public Boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
