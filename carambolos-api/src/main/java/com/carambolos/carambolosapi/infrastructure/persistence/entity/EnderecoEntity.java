package com.carambolos.carambolosapi.infrastructure.persistence.entity;

import com.carambolos.carambolosapi.system.security.CryptoAttributeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Schema(description = "Entidade que representa um endereço de um usuário.")
@Table(name = "endereco")
public class EnderecoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    @Convert(converter = CryptoAttributeConverter.class)
    private String cep;

    @Convert(converter = CryptoAttributeConverter.class)
    private String estado;

    @Convert(converter = CryptoAttributeConverter.class)
    private String cidade;

    @Convert(converter = CryptoAttributeConverter.class)
    private String bairro;

    @Convert(converter = CryptoAttributeConverter.class)
    private String logradouro;

    @Convert(converter = CryptoAttributeConverter.class)
    private String numero;

    @Convert(converter = CryptoAttributeConverter.class)
    private String complemento;

    @Convert(converter = CryptoAttributeConverter.class)
    private String referencia;

    @Column(name = "is_ativo")
    private boolean isAtivo = true;

    @Column(name = "usuario_id")
    private Integer usuario;

    @Column(name = "dedup_hash", length = 64)
    private String dedupHash;

    public EnderecoEntity(int id, String nome, String cep, String estado, String cidade, String bairro, String logradouro, String numero, String complemento, String referencia, boolean isAtivo, Integer usuario, String dedupHash) {
        this.id = id;
        this.nome = nome;
        this.cep = cep;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.referencia = referencia;
        this.isAtivo = isAtivo;
        this.usuario = usuario;
        this.dedupHash = dedupHash;
    }

    public EnderecoEntity() {}

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
