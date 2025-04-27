package com.carambolos.carambolosapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {
    @Email
    @NotBlank(message = "email não pode ser nulo")
    @Schema(description = "Endereço de e-mail do usuário", example = "luciana-bernardes@gmail.com")
    private String email;

    @NotBlank
    @Size(min = 6)
    @Schema(description = "Senha do usuário (mínimo 6 caracteres)", example = "gD8otDamss")
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
