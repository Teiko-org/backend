package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.Endereco;

public class EnderecoResponseDTO {
    private int id;
    private String cep;
    private String estado;
    private String cidade;
    private String bairro;
    private String logradouro;
    private String numero;
    private String complemento;
    private String referencia;
    private boolean isAtivo;
    private Integer usuario;

    public static EnderecoResponseDTO toResponseDTO(Endereco endereco) {
        EnderecoResponseDTO responseDto = new EnderecoResponseDTO();

        responseDto.setId(endereco.getId());
        responseDto.setCep(endereco.getCep());
        responseDto.setEstado(endereco.getEstado());
        responseDto.setCidade(endereco.getCidade());
        responseDto.setBairro(endereco.getBairro());
        responseDto.setLogradouro(endereco.getLogradouro());
        responseDto.setNumero(endereco.getNumero());
        responseDto.setComplemento(endereco.getComplemento());
        responseDto.setReferencia(endereco.getReferencia());
        responseDto.setAtivo(endereco.isAtivo());
        responseDto.setUsuario(endereco.getUsuario());

        return responseDto;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }
}
