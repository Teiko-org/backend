package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.AdicionalUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.AdicionalMapper;
import com.carambolos.carambolosapi.infrastructure.web.response.AdicionalResponseDTO;
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

    @GetMapping
    public ResponseEntity<List<AdicionalResponseDTO>> listarAdicionais() {
        return ResponseEntity.ok().body(
                adicionalMapper.toResponseDTO(
                        adicionalUseCase.listarAdicionais()
                )
        );
    }
}
