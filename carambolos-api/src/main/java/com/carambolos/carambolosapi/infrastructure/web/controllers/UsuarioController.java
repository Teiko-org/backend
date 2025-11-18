package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.UsuarioUseCase;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.AzureStorageGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.UsuarioMapper;
import com.carambolos.carambolosapi.infrastructure.web.request.AlterarSenhaRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.request.AtualizarUsuarioRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.request.LoginRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.request.UsuarioRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.UsuarioResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.UsuarioTokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuário Controller", description = "Gerencia usuários do sistema")
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final AzureStorageGatewayImpl azureStorageGateway;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioUseCase usuarioUseCase, AzureStorageGatewayImpl azureStorageGateway, UsuarioMapper usuarioMapper) {
        this.usuarioUseCase = usuarioUseCase;
        this.azureStorageGateway = azureStorageGateway;
        this.usuarioMapper = usuarioMapper;
    }

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
        List<Usuario> usuarios = usuarioUseCase.listar();
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<UsuarioResponseDTO> usuariosResponse = usuarios.stream()
                .map(UsuarioMapper::toResponseDTO)
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
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @PathVariable Integer id) {
        Usuario usuario = usuarioUseCase.buscarPorId(id);
        UsuarioResponseDTO usuarioResponse = UsuarioMapper.toResponseDTO(usuario);
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
        Usuario usuario = UsuarioMapper.toDomain(usuarioRequest);
        Usuario usuarioRegistrado = usuarioUseCase.cadastrar(usuario);
        UsuarioResponseDTO usuarioResponse = UsuarioMapper.toResponseDTO(usuarioRegistrado);
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
    public ResponseEntity<UsuarioTokenDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletResponse response
    ) {
        final Usuario usuario = UsuarioMapper.toDomain(loginRequestDTO);
        UsuarioTokenDTO usuarioToken = usuarioUseCase.autenticar(usuario, response);
        return ResponseEntity.status(200).body(usuarioToken);
    }

    @PostMapping("/logOut")
    public ResponseEntity<Void> logOut(
            HttpServletResponse response,
            @CookieValue(value = "authToken", required = false) String token
    ) {
        usuarioUseCase.logOut(response, token.replace("Bearer ", ""));
        return ResponseEntity.ok().build();
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
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        Usuario usuario = UsuarioMapper.toDomain(usuarioRequest);
        Usuario usuarioAtualizado = usuarioUseCase.atualizar(id, usuario);
        UsuarioResponseDTO usuarioResponse = UsuarioMapper.toResponseDTO(usuarioAtualizado);
        return ResponseEntity.status(200).body(usuarioResponse);
    }

    @Operation(
            summary = "Atualizar dados pessoais do usuário",
            description = "Atualiza apenas os dados pessoais (nome, telefone, data de nascimento, gênero) do usuário."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Telefone já em uso")
    })
    @PatchMapping("/{id}/dados-pessoais")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<UsuarioResponseDTO> atualizarDadosPessoais(
            @PathVariable Integer id,
            @Valid @RequestBody AtualizarUsuarioRequestDTO atualizarUsuarioRequest) {
        Usuario usuario = UsuarioMapper.toDomain(atualizarUsuarioRequest);
        Usuario usuarioAtualizado = usuarioUseCase.atualizarDadosPessoais(id, usuario);
        UsuarioResponseDTO usuarioResponse = UsuarioMapper.toResponseDTO(usuarioAtualizado);
        return ResponseEntity.status(200).body(usuarioResponse);
    }


    @Operation(
            summary = "Alterar senha do usuário",
            description = "Permite que o usuário altere sua senha informando a senha atual e a nova senha."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Senha atual incorreta"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PatchMapping("/{id}/alterar-senha")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable Integer id,
            @Valid @RequestBody AlterarSenhaRequestDTO alterarSenhaRequest) {
        usuarioUseCase.alterarSenha(id, alterarSenhaRequest.getSenhaAtual(), alterarSenhaRequest.getNovaSenha());
        return ResponseEntity.ok().build();
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
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        usuarioUseCase.deletar(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(
            summary = "Upload de imagem de perfil do usuário",
            description = "Faz upload da imagem de perfil do usuário para o Azure Storage e atualiza a URL no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem de perfil atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Arquivo inválido ou formato não suportado",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content())
    })
    @PostMapping(value = "/{id}/upload-imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<UsuarioResponseDTO> uploadImagemPerfil(
            @PathVariable Integer id,
            @RequestPart("file") MultipartFile file) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().build();
            }

            String imageUrl = azureStorageGateway.upload(file);

            Usuario usuarioAtualizado = usuarioUseCase.atualizarImagemPerfil(id, imageUrl);
            UsuarioResponseDTO usuarioResponse = UsuarioMapper.toResponseDTO(usuarioAtualizado);

            return ResponseEntity.ok(usuarioResponse);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
