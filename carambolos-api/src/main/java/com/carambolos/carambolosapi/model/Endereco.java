package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;
import com.carambolos.carambolosapi.utils.CryptoAttributeConverter;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description = "Entidade que representa um endereço de um usuário.")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do endereço", example = "1")
    private int id;

    @Schema(description = "Nome do endereço (ex: Casa, Trabalho)", example = "Casa")
    private String nome;

    @Schema(description = "CEP do endereço (apenas números)", example = "12345678")
    @Convert(converter = CryptoAttributeConverter.class)
    private String cep;

    @Schema(description = "Estado do endereço", example = "SP")
    @Convert(converter = CryptoAttributeConverter.class)
    private String estado;

    @Schema(description = "Cidade do endereço", example = "São Paulo")
    @Convert(converter = CryptoAttributeConverter.class)
    private String cidade;

    @Schema(description = "Bairro do endereço", example = "Centro")
    @Convert(converter = CryptoAttributeConverter.class)
    private String bairro;

    @Schema(description = "Logradouro do endereço (rua, avenida, etc.)", example = "Rua das Flores")
    @Convert(converter = CryptoAttributeConverter.class)
    private String logradouro;

    @Schema(description = "Número do endereço", example = "123")
    @Convert(converter = CryptoAttributeConverter.class)
    private String numero;

    @Schema(description = "Complemento do endereço", example = "Apartamento 202")
    @Convert(converter = CryptoAttributeConverter.class)
    private String complemento;

    @Schema(description = "Referência para localização", example = "Próximo à praça central")
    @Convert(converter = CryptoAttributeConverter.class)
    private String referencia;

    @Column(name = "is_ativo")
    private boolean isAtivo = true;

    @Column(name = "usuario_id")
    @Schema(description = "ID do usuário associado ao endereço", example = "5")
    private Integer usuario;

    @Column(name = "dedup_hash", length = 64)
    private String dedupHash;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public String getDedupHash() {
        return dedupHash;
    }

    public void setDedupHash(String dedupHash) {
        this.dedupHash = dedupHash;
    }

    public Boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
    }
}
