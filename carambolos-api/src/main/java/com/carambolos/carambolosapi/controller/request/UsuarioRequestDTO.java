package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioRequestDTO {
    @NotBlank
    private String nome;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 6)
    private String senha;
    @NotBlank
    @Size(max = 14)
    private String contato;

    public static Usuario toEntity(UsuarioRequestDTO requestDto) {
        Usuario usuario = new Usuario();

        if(requestDto == null) {
            return null;
        }

        usuario.setNome(requestDto.getNome());
        usuario.setEmail(requestDto.getEmail());
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

    public @Email @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotBlank String email) {
        this.email = email;
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
