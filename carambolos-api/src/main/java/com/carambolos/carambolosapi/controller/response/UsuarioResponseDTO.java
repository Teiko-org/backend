package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.Usuario;

public class UsuarioResponseDTO {
    private Integer id;
    private String nome;
    private String email;
    private String contato;
    private boolean isAtivo;

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioResponseDTO responseDto = new UsuarioResponseDTO();

        responseDto.setId(usuario.getId());
        responseDto.setNome(usuario.getNome());
        responseDto.setEmail(usuario.getEmail());
        responseDto.setContato(usuario.getContato());
        responseDto.setAtivo(usuario.isAtivo());

        return responseDto;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
    }
}
