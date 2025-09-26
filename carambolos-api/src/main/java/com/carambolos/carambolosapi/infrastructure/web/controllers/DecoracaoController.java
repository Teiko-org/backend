package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.infrastructure.web.request.DecoracaoRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.DecoracaoResponseDTO;
import com.carambolos.carambolosapi.application.usecases.DecoracaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/decoracoes")
@Tag(name = "Decoração Controller", description = "Gerencia decorações de bolos")
@SecurityRequirement(name = "Bearer")
public class DecoracaoController {

    @Autowired
    private DecoracaoService decoracaoService;

    @Operation(summary = "Cadastrar decoração", description = "Cadastra uma nova decoração de bolo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Decoração cadastrada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DecoracaoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DecoracaoResponseDTO> criar(
            @RequestPart("nome") String nome,
            @RequestPart("observacao") String observacao,
            @RequestPart(value = "categoria", required = false) String categoria,
            @RequestPart("imagens") MultipartFile[] imagens) {

        DecoracaoResponseDTO decoracao = decoracaoService.cadastrar(nome, observacao, categoria, imagens);
        return ResponseEntity.ok(decoracao);
    }

    @Operation(summary = "Listar decorações ativas", description = "Retorna todas as decorações de bolo ativas no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de decorações retornada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DecoracaoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "204", description = "Nenhuma decoração encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<DecoracaoResponseDTO>> listarAtivas() {
        List<DecoracaoResponseDTO> decoracoes = decoracaoService.listarAtivas();
        if (decoracoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(decoracoes);
    }

    @Operation(summary = "Listar pré-decorações para Home", description = "Retorna decorações ativas com categoria não nula")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DecoracaoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "204", description = "Nenhuma decoração encontrada", content = @Content)
    })
    @GetMapping("/featured")
    public ResponseEntity<List<DecoracaoResponseDTO>> listarFeatured() {
        List<DecoracaoResponseDTO> decoracoes = decoracaoService.listarAtivasComCategoria();
        if (decoracoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(decoracoes);
    }

    @Operation(summary = "Buscar decoração por ID", description = "Retorna uma decoração pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Decoração retornada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DecoracaoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "404", description = "Decoração não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DecoracaoResponseDTO> buscarPorId(@PathVariable Integer id) {
        DecoracaoResponseDTO decoracao = decoracaoService.buscarPorId(id);
        return ResponseEntity.ok(decoracao);
    }

    @Operation(summary = "Atualizar decoração", description = "Atualiza os dados de uma decoração existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Decoração atualizada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DecoracaoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Decoração não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<DecoracaoResponseDTO> atualizar(@PathVariable Integer id,
                                                          @RequestBody DecoracaoRequestDTO request) {
        DecoracaoResponseDTO decoracaoAtualizada = decoracaoService.atualizar(id, request);
        return ResponseEntity.ok(decoracaoAtualizada);
    }

    @Operation(summary = "Desativar decoração", description = "Desativa uma decoração pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Decoração desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Decoração não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Integer id) {
        decoracaoService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reativar decoração", description = "Reativa uma decoração desativada pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Decoração reativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Decoração não encontrada"),
    })
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Integer id) {
        decoracaoService.reativar(id);
        return ResponseEntity.noContent().build();
    }
}
