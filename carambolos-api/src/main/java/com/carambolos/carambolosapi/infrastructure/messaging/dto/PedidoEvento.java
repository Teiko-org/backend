package com.carambolos.carambolosapi.infrastructure.messaging.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class PedidoEvento {
    private String evento; 
    private Integer pedidoId;
    private Integer clienteId;
    private BigDecimal valorTotal;
    private OffsetDateTime dataEvento;
    private String origem;
    private String correlationId;
    private List<ItemPedido> itens;

    public static class ItemPedido {
        private Integer produtoId;
        private Integer quantidade;

        public Integer getProdutoId() { return produtoId; }
        public void setProdutoId(Integer produtoId) { this.produtoId = produtoId; }
        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    }

    public String getEvento() { return evento; }
    public void setEvento(String evento) { this.evento = evento; }
    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }
    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public OffsetDateTime getDataEvento() { return dataEvento; }
    public void setDataEvento(OffsetDateTime dataEvento) { this.dataEvento = dataEvento; }
    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
}


