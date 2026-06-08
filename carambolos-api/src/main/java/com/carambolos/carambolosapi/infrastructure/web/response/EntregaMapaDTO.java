package com.carambolos.carambolosapi.infrastructure.web.response;

public class EntregaMapaDTO {
    private Integer resumoPedidoId;
    private String nomeCliente;
    private String telefoneCliente;
    private String enderecoCompleto;
    private Double latitude;
    private Double longitude;
    private String tipoPedido;
    private String observacoes;
    private String status;

    public EntregaMapaDTO(Integer resumoPedidoId, String nomeCliente, String telefoneCliente, String enderecoCompleto, Double latitude, Double longitude, String tipoPedido, String observacoes, String status) {
        this.resumoPedidoId = resumoPedidoId;
        this.nomeCliente = nomeCliente;
        this.telefoneCliente = telefoneCliente;
        this.enderecoCompleto = enderecoCompleto;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tipoPedido = tipoPedido;
        this.observacoes = observacoes;
        this.status = status;
    }

    public Integer getResumoPedidoId() { return resumoPedidoId; }
    public void setResumoPedidoId(Integer resumoPedidoId) { this.resumoPedidoId = resumoPedidoId; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public String getTelefoneCliente() { return telefoneCliente; }
    public void setTelefoneCliente(String telefoneCliente) { this.telefoneCliente = telefoneCliente; }
    public String getEnderecoCompleto() { return enderecoCompleto; }
    public void setEnderecoCompleto(String enderecoCompleto) { this.enderecoCompleto = enderecoCompleto; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getTipoPedido() { return tipoPedido; }
    public void setTipoPedido(String tipoPedido) { this.tipoPedido = tipoPedido; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
