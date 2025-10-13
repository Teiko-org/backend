package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.EnderecoUseCase;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.EnderecoMapper;
import com.carambolos.carambolosapi.infrastructure.web.request.EnderecoRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.EnderecoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
@Tag(name = "Endereço Controller", description = "Gerencia os endereços dos usuários")
@SecurityRequirement(name = "Bearer")
public class EnderecoController {

    private final EnderecoUseCase enderecoUseCase;
    private final EnderecoMapper enderecoMapper;

    public EnderecoController(EnderecoUseCase enderecoUseCase, EnderecoMapper enderecoMapper) {
        this.enderecoUseCase = enderecoUseCase;
        this.enderecoMapper = enderecoMapper;
    }

    @Operation(summary = "Lista todos os endereços")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum endereço encontrado", content = @Content())
    })
    @GetMapping
    public ResponseEntity<Page<EnderecoResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Endereco> enderecos = enderecoUseCase.listar(pageable);
        if (enderecos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        Page<EnderecoResponseDTO> enderecosResponse = enderecos.map(EnderecoMapper::toResponseDTO);
        return ResponseEntity.status(200).body(enderecosResponse);
    }

    @Operation(summary = "Busca um endereço por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado", content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> buscarPorId(@PathVariable Integer id) {
        Endereco endereco = enderecoUseCase.buscarPorId(id);
        EnderecoResponseDTO enderecoResponse = EnderecoMapper.toResponseDTO(endereco);
        return ResponseEntity.status(200).body(enderecoResponse);
    }

    @Operation(summary = "Cadastra um novo endereço")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Endereço cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content())
    })
    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> cadastrar(@Valid @RequestBody EnderecoRequestDTO enderecoRequest) {
        Endereco endereco = EnderecoMapper.toDomain(enderecoRequest);
        Endereco enderecoRegistrado = enderecoUseCase.cadastrar(endereco);
        EnderecoResponseDTO enderecoResponse = EnderecoMapper.toResponseDTO(enderecoRegistrado);
        return ResponseEntity.status(201).body(enderecoResponse);
    }

    @Operation(summary = "Atualiza um endereço existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content())
    })
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody EnderecoRequestDTO enderecoRequest
    ) {
        Endereco endereco = EnderecoMapper.toDomain(enderecoRequest);
        Endereco enderecoAtualizado = enderecoUseCase.atualizar(id, endereco);
        EnderecoResponseDTO enderecoResponse = EnderecoMapper.toResponseDTO(enderecoAtualizado);
        return ResponseEntity.status(200).body(enderecoResponse);
    }

    @Operation(summary = "Lista endereços de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de endereços do usuário retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum endereço encontrado para o usuário", content = @Content())
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EnderecoResponseDTO>> listarPorUsuario(@PathVariable Integer usuarioId) {
        List<Endereco> enderecos = enderecoUseCase.listarPorUsuario(usuarioId);
        if (enderecos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<EnderecoResponseDTO> enderecosResponse = enderecos.stream().map(EnderecoMapper::toResponseDTO).toList();
        return ResponseEntity.status(200).body(enderecosResponse);
    }

    @Operation(summary = "Deleta um endereço")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Endereço deletado com sucesso", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado", content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        enderecoUseCase.deletar(id);
        return ResponseEntity.status(204).build();
    }
}
