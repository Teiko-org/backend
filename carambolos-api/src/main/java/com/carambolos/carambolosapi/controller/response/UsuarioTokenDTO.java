package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.Usuario;

public class UsuarioTokenDTO {

    private Integer userId;
    private String nome;
    private String contato;
    private String token;
    private Boolean isAdmin;

    public static UsuarioTokenDTO toTokenDTO(Usuario usuario, String token) {
        UsuarioTokenDTO tokenDto = new UsuarioTokenDTO();

        tokenDto.setUserId(usuario.getId());
        tokenDto.setContato(usuario.getContato());
        tokenDto.setNome(usuario.getNome());
        tokenDto.setToken(token);
        tokenDto.setAdmin(usuario.sysAdmin);
        return tokenDto;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
