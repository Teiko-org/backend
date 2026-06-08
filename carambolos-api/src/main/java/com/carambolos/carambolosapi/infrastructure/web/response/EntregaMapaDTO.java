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
    private String logradouro;
    private String numero;
    private String cidade;
    private String estado;

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

    public EntregaMapaDTO(Integer resumoPedidoId, String nomeCliente, String telefoneCliente, String enderecoCompleto, Double latitude, Double longitude, String tipoPedido, String observacoes, String status, String logradouro, String numero, String cidade, String estado) {
        this(resumoPedidoId, nomeCliente, telefoneCliente, enderecoCompleto, latitude, longitude, tipoPedido, observacoes, status);
        this.logradouro = logradouro;
        this.numero = numero;
        this.cidade = cidade;
        this.estado = estado;
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
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
