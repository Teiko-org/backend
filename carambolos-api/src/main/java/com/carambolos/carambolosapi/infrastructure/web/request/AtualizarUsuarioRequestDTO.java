package com.carambolos.carambolosapi.infrastructure.web.request;

import com.carambolos.carambolosapi.domain.entity.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "DTO para requisição de atualização de dados do usuário.")
public class AtualizarUsuarioRequestDTO {

    @Schema(description = "Nome completo do usuário.", example = "Ana Souza")
    @NotBlank
    private String nome;

    @Schema(description = "Número de telefone para contato (WhatsApp ou ligação).", example = "5511987654321")
    @NotBlank
    @Size(max = 14)
    private String contato;

    @Schema(description = "Data de nascimento do usuário.", example = "2000-05-20")
    private LocalDate dataNascimento;

    @Schema(description = "Gênero do usuário.", example = "Feminino")
    private String genero;

    public static Usuario toEntity(AtualizarUsuarioRequestDTO requestDto) {
        if(requestDto == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setNome(requestDto.getNome());
        usuario.setContato(requestDto.getContato());
        usuario.setDataNascimento(requestDto.getDataNascimento());
        usuario.setGenero(requestDto.getGenero());
        return usuario;
    }

    public @NotBlank String getNome() {
        return nome;
    }

    public void setNome(@NotBlank String nome) {
        this.nome = nome;
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