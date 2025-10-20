package com.carambolos.carambolosapi.infrastructure.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

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

    @Schema(description = "Data de nascimento do usuário.", example = "2000-05-20")
    private LocalDate dataNascimento;

    @Schema(description = "Gênero do usuário.", example = "Feminino")
    private String genero;

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
}