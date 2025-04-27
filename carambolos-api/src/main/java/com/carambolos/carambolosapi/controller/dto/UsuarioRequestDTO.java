package com.carambolos.carambolosapi.controller.dto;

import com.carambolos.carambolosapi.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para requisição de cadastro de usuário.")
public class UsuarioRequestDTO {

    @Schema(description = "Nome completo do usuário.", example = "Ana Souza")
    @NotBlank
    private String nome;

    @Schema(description = "Número de telefone para contato (WhatsApp ou ligação).", example = "5511987654321")
    @NotBlank
    @Size(max = 14)
    private String contato;

    @Schema(description = "Senha de acesso do usuário. Mínimo de 6 caracteres.", example = "senhaSegura123")
    @NotBlank
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
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
