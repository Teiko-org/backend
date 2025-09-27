package com.carambolos.carambolosapi.domain.entity;

import java.time.LocalDate;

public class Usuario {
    private Integer id;
    private String nome;
    private String contato;
    private String senha;
    private LocalDate dataNascimento;
    private String genero;
    private String imagemUrl;
    private boolean isAtivo = true;
    public Boolean sysAdmin;

    public Usuario(Integer id, String nome, String contato, String senha, LocalDate dataNascimento, String genero, String imagemUrl, boolean isAtivo, Boolean sysAdmin) {
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

    public Usuario() {
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

    public void setSysAdmin(Boolean sysAdmin) {
        this.sysAdmin = sysAdmin;
    }
}
