package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.controller.dto.LoginRequestDTO;
import com.carambolos.carambolosapi.controller.dto.UsuarioRequestDTO;
import com.carambolos.carambolosapi.controller.dto.UsuarioResponseDTO;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuário Controller", description = "Gerencia usuários do sistema")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna a lista de todos os usuários cadastrados no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado",
                    content = @Content())
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        List<Usuario> usuarios = usuarioService.listar();
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<UsuarioResponseDTO> usuariosResponse = usuarios.stream()
                .map(UsuarioResponseDTO::toResponseDTO)
                .toList();
        return ResponseEntity.status(200).body(usuariosResponse);
    }

    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados de um usuário específico com base no ID informado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        UsuarioResponseDTO usuarioResponse = UsuarioResponseDTO.toResponseDTO(usuario);
        return ResponseEntity.status(200).body(usuarioResponse);
    }

    @Operation(
            summary = "Cadastrar novo usuário",
            description = "Cadastra um novo usuário no sistema utilizando os dados fornecidos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "E-mail ou telefone já cadastrado",
                    content = @Content())
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        Usuario usuario = UsuarioRequestDTO.toEntity(usuarioRequest);
        Usuario usuarioRegistrado = usuarioService.cadastrar(usuario);
        UsuarioResponseDTO usuarioResponse = UsuarioResponseDTO.toResponseDTO(usuarioRegistrado);
        return ResponseEntity.status(201).body(usuarioResponse);
    }

    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza a autenticação de um usuário no sistema com e-mail e senha."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content())
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        usuarioService.login(loginRequestDTO.getEmail(), loginRequestDTO.getSenha());
        return ResponseEntity.status(200).body("Usuário autenticado com sucesso");
    }

    @Operation(
            summary = "Atualizar dados do usuário",
            description = "Atualiza as informações de um usuário existente com base no ID informado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "409", description = "E-mail ou telefone já em uso",
                    content = @Content())
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody UsuarioRequestDTO usuarioRequest) {
        Usuario usuario = UsuarioRequestDTO.toEntity(usuarioRequest);
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
        UsuarioResponseDTO usuarioResponse = UsuarioResponseDTO.toResponseDTO(usuarioAtualizado);
        return ResponseEntity.status(200).body(usuarioResponse);
    }

    @Operation(
            summary = "Deletar usuário",
            description = "Remove um usuário do sistema com base no ID informado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        usuarioService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}
