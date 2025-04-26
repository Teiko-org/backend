package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioRequestDTO {
    @NotBlank
    private String nome;
    @NotBlank
    @Size(max = 14)
    private String contato;
    @NotBlank
    @Size(min = 6)
    private String senha;

    public static Usuario toEntity(UsuarioRequestDTO requestDto) {
        Usuario usuario = new Usuario();

        if(requestDto == null) {
            return null;
        }

        usuario.setNome(requestDto.getNome());
        usuario.setSenha(requestDto.getSenha());
        usuario.setContato(requestDto.getContato());

        return usuario;
    }


    public @NotBlank String getNome() {
        return nome;
    }

    public void setNome(@NotBlank String nome) {
        this.nome = nome;
    }


    public @NotBlank @Size(min = 6) String getSenha() {
        return senha;
    }

    public void setSenha(@NotBlank @Size(min = 6) String senha) {
        this.senha = senha;
    }

    public @NotBlank @Size(max = 14) String getContato() {
        return contato;
    }

    public void setContato(@NotBlank @Size(max = 14) String contato) {
        this.contato = contato;
    }
}
