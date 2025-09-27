package com.carambolos.carambolosapi.infrastructure.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {

    @NotBlank(message = "contato não pode ser nulo")
    @Size(max = 14)
    @Schema(description = "Contato do usuário", example = "11995843106")
    private String contato;

    @NotBlank
    @Size(min = 6)
    @Schema(description = "Senha do usuário (mínimo 6 caracteres)", example = "gD8otDamss")
    private String senha;

    public @NotBlank(message = "contato não pode ser nulo") @Size(max = 14) String getContato() {
        return contato;
    }

    public void setContato(@NotBlank(message = "contato não pode ser nulo") @Size(max = 14) String contato) {
        this.contato = contato;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
