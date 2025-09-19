package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.entity.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta contendo informações do endereço de um usuário")
public class EnderecoResponseDTO {

    @Schema(description = "ID do endereço", example = "1")
    private int id;

    @Schema(description = "CEP do endereço (8 dígitos numéricos)", example = "12345678")
    private String cep;

    @Schema(description = "Nome do endereço (ex: Casa, Trabalho)", example = "Casa")
    private String nome;

    @Schema(description = "Estado do endereço", example = "SP")
    private String estado;

    @Schema(description = "Cidade do endereço", example = "São Paulo")
    private String cidade;

    @Schema(description = "Bairro do endereço", example = "Centro")
    private String bairro;

    @Schema(description = "Logradouro do endereço", example = "Rua das Flores")
    private String logradouro;

    @Schema(description = "Número do endereço", example = "123")
    private String numero;

    @Schema(description = "Complemento do endereço", example = "Apartamento 202")
    private String complemento;

    @Schema(description = "Referência do endereço", example = "Próximo à praça central")
    private String referencia;
    private boolean isAtivo;

    @Schema(description = "ID do usuário associado ao endereço", example = "5")
    private Integer usuario;

    public static EnderecoResponseDTO toResponseDTO(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        EnderecoResponseDTO responseDto = new EnderecoResponseDTO();
        responseDto.setId(endereco.getId());
        responseDto.setCep(endereco.getCep());
        responseDto.setNome(endereco.getNome());
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
