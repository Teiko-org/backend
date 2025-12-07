package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.AdicionalDecoracaoUseCase;
import com.carambolos.carambolosapi.application.usecases.DecoracaoUseCase;
import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracaoSummary;
import com.carambolos.carambolosapi.domain.entity.Decoracao;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.AdicionalDecoracaoMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.DecoracaoMapper;
import com.carambolos.carambolosapi.infrastructure.web.request.DecoracaoRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.DecoracaoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/decoracoes")
@Tag(name = "Decoração Controller", description = "Gerencia decorações de bolos")
@SecurityRequirement(name = "Bearer")
public class DecoracaoController {
    private final DecoracaoUseCase decoracaoUseCase;
    private final DecoracaoMapper decoracaoMapper;
    private final AdicionalDecoracaoUseCase adicionalDecoracaoUseCase;
    private final AdicionalDecoracaoMapper adicionalDecoracaoMapper;

    public DecoracaoController(DecoracaoUseCase decoracaoUseCase, DecoracaoMapper decoracaoMapper, AdicionalDecoracaoUseCase adicionalDecoracaoUseCase, AdicionalDecoracaoMapper adicionalDecoracaoMapper) {
        this.decoracaoUseCase = decoracaoUseCase;
        this.decoracaoMapper = decoracaoMapper;
        this.adicionalDecoracaoUseCase = adicionalDecoracaoUseCase;
        this.adicionalDecoracaoMapper = adicionalDecoracaoMapper;
    }

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
            // Para usos públicos (referência de cliente), adicionais podem ser omitidos
            @RequestPart(value = "adicionais", required = false) String adicionais,
            @RequestPart("imagens") MultipartFile[] imagens)
    {
        List<Integer> adicionaisIds = (adicionais == null || adicionais.isBlank())
                ? List.of()
                : Arrays.stream(adicionais.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .toList();
        Decoracao decoracao = decoracaoUseCase.cadastrar(nome, observacao, categoria, adicionaisIds, imagens);
        return ResponseEntity.ok(decoracaoMapper.toResponse(decoracao));
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
        List<Decoracao> decoracoes = decoracaoUseCase.listarAtivas();
        if (decoracoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(decoracaoMapper.toResponse(decoracoes));
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
        List<Decoracao> decoracoes = decoracaoUseCase.listarAtivasComCategoria();
        if (decoracoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(decoracaoMapper.toResponse(decoracoes));
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
        Decoracao decoracao = decoracaoUseCase.buscarPorId(id);
        return ResponseEntity.ok(decoracaoMapper.toResponse(decoracao));
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
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DecoracaoResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestPart("nome") String nome,
            @RequestPart("observacao") String observacao,
            @RequestPart(value = "categoria", required = false) String categoria,
            // Para usos públicos (referência de cliente), adicionais podem ser omitidos
            @RequestPart(value = "adicionais", required = false) String adicionais,
            @RequestPart("imagens") MultipartFile[] imagens
    ) {
        List<Integer> adicionaisIds = (adicionais == null || adicionais.isBlank())
                ? List.of()
                : Arrays.stream(adicionais.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .toList();
        Decoracao decoracaoAtualizada = decoracaoUseCase.atualizar(id, nome, observacao, categoria, adicionaisIds, imagens);
        return ResponseEntity.ok(decoracaoMapper.toResponse(decoracaoAtualizada));
    }

    @Operation(summary = "Desativar decoração", description = "Desativa uma decoração pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Decoração desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Decoração não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Integer id) {
        decoracaoUseCase.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reativar decoração", description = "Reativa uma decoração desativada pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Decoração reativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Decoração não encontrada"),
    })
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Integer id) {
        decoracaoUseCase.reativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar adicionais por decoração", description = "Retorna todos os adicionais associados a cada decoração")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de adicionais retornada com sucesso", content = @Content(
                    mediaType = "application/json"
            )),
            @ApiResponse(responseCode = "404", description = "Adicionais de decoração não encontrados", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/adicionais")
    public ResponseEntity<List<AdicionalDecoracaoSummary>> listarAdicionaisPorDecoracao() {
        List<AdicionalDecoracaoSummary> adicionais = adicionalDecoracaoUseCase.buscarAdicionaisPorDecoracao();

        if (adicionais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(adicionais);
    }
}
