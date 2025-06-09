package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para criação de endereço de um usuário")
public class EnderecoRequestDTO {

    @NotBlank
    @Pattern(regexp = "\\d{8}", message = "O CEP deve ter exatamente 8 dígitos numéricos.")
    @Schema(description = "CEP do endereço (8 dígitos numéricos)", example = "12345678")
    private String cep;

    @Size(max = 20)
    @Schema(description = "Nome do endereço (ex: Casa, Trabalho)", example = "Casa")
    private String nome;

    @NotBlank
    @Size(max = 20)
    @Schema(description = "Estado do endereço", example = "SP")
    private String estado;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Cidade do endereço", example = "São Paulo")
    private String cidade;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Bairro do endereço", example = "Centro")
    private String bairro;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Logradouro do endereço", example = "Rua das Flores")
    private String logradouro;

    @NotBlank
    @Size(max = 6)
    @Schema(description = "Número do endereço", example = "123")
    private String numero;

    @Size(max = 20)
    @Schema(description = "Complemento do endereço", example = "Apartamento 202")
    private String complemento;

    @Size(max = 70)
    @Schema(description = "Referência do endereço", example = "Próximo à praça central")
    private String referencia;

    @Column(name = "usuario_id")
    @Schema(description = "ID do usuário associado ao endereço", example = "1")
    private Integer usuario;

    public static Endereco toEntity(EnderecoRequestDTO requestDto) {
        if (requestDto == null) {
            return null;
        }

        Endereco endereco = new Endereco();
        endereco.setCep(requestDto.getCep());
        endereco.setNome(requestDto.getNome());
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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }
}
