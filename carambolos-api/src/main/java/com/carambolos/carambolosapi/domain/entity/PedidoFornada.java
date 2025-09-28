package com.carambolos.carambolosapi.domain.entity;
import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;
import java.time.LocalDate;

public class PedidoFornada {
    private Integer id;
    private Integer fornadaDaVez;
    private Integer endereco;
    private Integer usuario;
    private Integer quantidade;
    private LocalDate dataPrevisaoEntrega;
    private TipoEntregaEnum tipoEntrega;
    private String nomeCliente;
    private String telefoneCliente;
    private String horarioRetirada;
    private String observacoes;
    private Boolean isAtivo;

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

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }

    public String getHorarioRetirada() {
        return horarioRetirada;
    }

    public void setHorarioRetirada(String horarioRetirada) {
        this.horarioRetirada = horarioRetirada;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getisAtivo() {
        return isAtivo;
    }

    public void setisAtivo(Boolean isAtivo) {
        this.isAtivo = isAtivo;
    }
}
