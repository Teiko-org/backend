package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.controller.dto.EnderecoRequestDTO;
import com.carambolos.carambolosapi.controller.dto.EnderecoResponseDTO;
import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.service.EnderecoService;
import com.carambolos.carambolosapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
@Tag(name = "Endereço Controller", description = "Gerencia os endereços dos usuários")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Lista todos os endereços")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum endereço encontrado", content = @Content())
    })
    @GetMapping
    public ResponseEntity<List<EnderecoResponseDTO>> listar() {
        List<Endereco> enderecos = enderecoService.listar();
        if (enderecos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<EnderecoResponseDTO> enderecosResponse = enderecos.stream().map(EnderecoResponseDTO::toResponseDTO).toList();
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
        Endereco endereco = enderecoService.buscarPorId(id);
        EnderecoResponseDTO enderecoResponse = EnderecoResponseDTO.toResponseDTO(endereco);
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
        Endereco endereco = EnderecoRequestDTO.toEntity(enderecoRequest);
        Endereco enderecoRegistrado = enderecoService.cadastrar(endereco);
        EnderecoResponseDTO enderecoResponse = EnderecoResponseDTO.toResponseDTO(enderecoRegistrado);
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
            @RequestBody EnderecoRequestDTO enderecoRequest
    ) {
        Endereco endereco = EnderecoRequestDTO.toEntity(enderecoRequest);
        Endereco enderecoAtualizado = enderecoService.atualizar(id, endereco);
        EnderecoResponseDTO enderecoResponse = EnderecoResponseDTO.toResponseDTO(enderecoAtualizado);
        return ResponseEntity.status(200).body(enderecoResponse);
    }

    @Operation(summary = "Deleta um endereço")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Endereço deletado com sucesso", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado", content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        enderecoService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}
