package com.carambolos.carambolosapi.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String contato;

    private String senha;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "genero")
    private String genero;

    @Column(name = "imagem_url")
    private String imagemUrl;

    @Column(name = "is_ativo")
    private boolean isAtivo = true;

    @Column(name = "sys_admin")
    public Boolean sysAdmin;

    public UsuarioEntity(Integer id, String nome, String contato, String senha, LocalDate dataNascimento, String genero, String imagemUrl, boolean isAtivo, Boolean sysAdmin) {
        this.id = id;
        this.nome = nome;
        this.contato = contato;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.imagemUrl = imagemUrl;
        this.isAtivo = isAtivo;
        this.sysAdmin = sysAdmin;
    }

    public UsuarioEntity() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
    }

    public Boolean getSysAdmin() {
        return sysAdmin;
    }
}
