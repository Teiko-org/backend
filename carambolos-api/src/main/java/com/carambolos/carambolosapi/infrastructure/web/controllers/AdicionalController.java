package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.AdicionalUseCase;
import com.carambolos.carambolosapi.domain.entity.Adicional;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.AdicionalMapper;
import com.carambolos.carambolosapi.infrastructure.web.response.AdicionalResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adicionais")
public class AdicionalController {
    private final AdicionalUseCase adicionalUseCase;
    private final AdicionalMapper adicionalMapper;

    public AdicionalController(AdicionalUseCase adicionalUseCase, AdicionalMapper adicionalMapper) {
        this.adicionalUseCase = adicionalUseCase;
        this.adicionalMapper = adicionalMapper;
    }

    @Operation(summary = "Adicionais por decoração", description = "Retorna todos os adicionais ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de adicionais retornada com sucesso", content = @Content(
                    mediaType = "application/json"
            )),
            @ApiResponse(responseCode = "404", description = "Adicionais não encontrados", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AdicionalResponseDTO>> listarAdicionais() {
        List<Adicional> adicionais = adicionalUseCase.listarAdicionais();

        if (adicionais.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(adicionalMapper.toResponseDTO(adicionais));
    }
}
