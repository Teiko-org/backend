package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.entity.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO de resposta contendo informações do usuário")
public class UsuarioResponseDTO {
    @Schema(description = "ID do usuário", example = "1")
    private Integer id;

    @Schema(description = "Nome do usuário", example = "Luciana Bernardes")
    private String nome;

    @Schema(description = "Celular do usuário", example = "556826852415")
    private String contato;

    @Schema(description = "Data de nascimento do usuário", example = "2000-05-20")
    private LocalDate dataNascimento;

    @Schema(description = "Gênero do usuário", example = "Feminino")
    private String genero;

    @Schema(description = "URL da imagem de perfil do usuário")
    private String imagemUrl;

    private boolean isAtivo;

    private boolean isAdmin;

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioResponseDTO responseDto = new UsuarioResponseDTO();

        responseDto.setId(usuario.getId());
        responseDto.setNome(usuario.getNome());
        responseDto.setContato(usuario.getContato());
        responseDto.setDataNascimento(usuario.getDataNascimento());
        responseDto.setGenero(usuario.getGenero());
        responseDto.setImagemUrl(usuario.getImagemUrl());
        responseDto.setAtivo(usuario.isAtivo());
        if (usuario.getSysAdmin() == null) {
            responseDto.setAdmin(false);
        } else {
            responseDto.setAdmin(usuario.getSysAdmin());
        }

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

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
