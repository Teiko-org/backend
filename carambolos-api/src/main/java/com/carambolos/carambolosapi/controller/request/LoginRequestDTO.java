package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {
    @NotBlank
    @Size(max = 14)
    private String contato;
    @NotBlank
    @Size(min = 6)
    private String senha;

    public static Usuario toEntity(LoginRequestDTO loginDto) {
        Usuario usuario = new Usuario();

        usuario.setContato(loginDto.getContato());
        usuario.setSenha(loginDto.getSenha());

        return usuario;
    }

    public @NotBlank @Size(max = 14) String getContato() {
        return contato;
    }

    public void setContato(@NotBlank @Size(max = 14) String contato) {
        this.contato = contato;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
