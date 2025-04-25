package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EnderecoRequestDTO {
    @NotBlank
    @Pattern(regexp = "\\d{8}", message = "O CEP deve ter exatamente 8 dígitos numéricos.")
    private String cep;

    @NotBlank
    @Size(max = 20)
    private String estado;

    @NotBlank
    @Size(max = 100)
    private String cidade;

    @NotBlank
    @Size(max = 100)
    private String bairro;

    @NotBlank
    @Size(max = 100)
    private String logradouro;

    @NotBlank
    @Size(max = 6)
    private String numero;

    @Size(max = 20)
    private String complemento;

    @Size(max = 70)
    private String referencia;

    private Integer usuario;

    public static Endereco toEntity(EnderecoRequestDTO requestDto) {
        Endereco endereco = new Endereco();

        if(requestDto == null) {
            return null;
        }

        endereco.setCep(requestDto.getCep());
        endereco.setEstado(requestDto.getEstado());
        endereco.setCidade(requestDto.getCidade());
        endereco.setBairro(requestDto.getBairro());
        endereco.setLogradouro(requestDto.getLogradouro());
        endereco.setNumero(requestDto.getNumero());
        endereco.setComplemento(requestDto.getComplemento());
        endereco.setReferencia(requestDto.getReferencia());
        endereco.setUsuario(requestDto.getUsuario());

        return endereco;
    }

    public @NotBlank @Pattern(regexp = "\\d{8}", message = "O CEP deve ter exatamente 8 dígitos numéricos.") String getCep() {
        return cep;
    }

    public void setCep(@NotBlank @Pattern(regexp = "\\d{8}", message = "O CEP deve ter exatamente 8 dígitos numéricos.") String cep) {
        this.cep = cep;
    }

    public @NotBlank @Size(max = 20) String getEstado() {
        return estado;
    }

    public void setEstado(@NotBlank @Size(max = 20) String estado) {
        this.estado = estado;
    }

    public @NotBlank @Size(max = 100) String getCidade() {
        return cidade;
    }

    public void setCidade(@NotBlank @Size(max = 100) String cidade) {
        this.cidade = cidade;
    }

    public @NotBlank @Size(max = 100) String getBairro() {
        return bairro;
    }

    public void setBairro(@NotBlank @Size(max = 100) String bairro) {
        this.bairro = bairro;
    }

    public @NotBlank @Size(max = 100) String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(@NotBlank @Size(max = 100) String logradouro) {
        this.logradouro = logradouro;
    }

    public @NotBlank @Size(max = 6) String getNumero() {
        return numero;
    }

    public void setNumero(@NotBlank @Size(max = 6) String numero) {
        this.numero = numero;
    }

    public @Size(max = 20) String getComplemento() {
        return complemento;
    }

    public void setComplemento(@Size(max = 20) String complemento) {
        this.complemento = complemento;
    }

    public @Size(max = 70) String getReferencia() {
        return referencia;
    }

    public void setReferencia(@Size(max = 70) String referencia) {
        this.referencia = referencia;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }
}
