package com.carambolos.carambolosapi.infrastructure.persistence.entity;

import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "resumo_pedido")
public class ResumoPedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private Double valor;

    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @Column(name = "pedido_fornada_id")
    private Integer pedidoFornadaId;

    @Column(name = "pedido_bolo_id")
    private Integer pedidoBoloId;

    @Column(name = "is_ativo")
    private Boolean isAtivo = true;

    public ResumoPedidoEntity() {
    }

    public ResumoPedidoEntity(
            Integer id,
            StatusEnum status,
            Double valor,
            LocalDateTime dataPedido,
            LocalDateTime dataEntrega,
            Integer pedidoFornadaId,
            Integer pedidoBoloId,
            Boolean isAtivo
    ) {
        this.id = id;
        this.status = status;
        this.valor = valor;
        this.dataPedido = dataPedido;
        this.dataEntrega = dataEntrega;
        this.pedidoFornadaId = pedidoFornadaId;
        this.pedidoBoloId = pedidoBoloId;
        this.isAtivo = isAtivo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Integer getPedidoFornadaId() {
        return pedidoFornadaId;
    }

    public void setPedidoFornadaId(Integer pedidoFornadaId) {
        this.pedidoFornadaId = pedidoFornadaId;
    }

    public Integer getPedidoBoloId() {
        return pedidoBoloId;
    }

    public void setPedidoBoloId(Integer pedidoBoloId) {
        this.pedidoBoloId = pedidoBoloId;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
