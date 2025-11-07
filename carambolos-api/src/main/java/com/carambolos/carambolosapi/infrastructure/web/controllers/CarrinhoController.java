package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.CarrinhoUseCases;
import com.carambolos.carambolosapi.domain.entity.Carrinho;
import com.carambolos.carambolosapi.infrastructure.web.request.CarrinhoRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.request.CartItemDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.CarrinhoResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
@Tag(name = "Carrinho Controller", description = "Gerencia o carrinho de compras dos usuários")
@SecurityRequirement(name = "Bearer")
public class CarrinhoController {

    private final CarrinhoUseCases carrinhoUseCases;
    private final ObjectMapper objectMapper;

    public CarrinhoController(CarrinhoUseCases carrinhoUseCases, ObjectMapper objectMapper) {
        this.carrinhoUseCases = carrinhoUseCases;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Buscar carrinho do usuário", description = "Retorna o carrinho do usuário logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrinho encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarrinhoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content())
    })
    @GetMapping
    public ResponseEntity<CarrinhoResponseDTO> buscarCarrinho(
            @RequestHeader("X-User-Id") Integer userId
    ) {
        try {
            Carrinho carrinho = carrinhoUseCases.buscarCarrinhoPorUsuario(userId);
            CarrinhoResponseDTO response = new CarrinhoResponseDTO();

            if (carrinho.getItens() != null && !carrinho.getItens().isEmpty() && !carrinho.getItens().equals("[]")) {
                try {
                    CartItemDTO[] itensArray = objectMapper.readValue(carrinho.getItens(), CartItemDTO[].class);
                    response.setItens(java.util.Arrays.asList(itensArray));
                } catch (Exception e) {
                    response.setItens(java.util.Collections.emptyList());
                }
            } else {
                response.setItens(java.util.Collections.emptyList());
            }

            response.setDataUltimaAtualizacao(carrinho.getDataUltimaAtualizacao());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Salvar/atualizar carrinho do usuário", description = "Salva ou atualiza o carrinho do usuário logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrinho salvo com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarrinhoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content())
    })
    @PutMapping
    public ResponseEntity<CarrinhoResponseDTO> salvarCarrinho(
            @RequestHeader("X-User-Id") Integer userId,
            @Valid @RequestBody CarrinhoRequestDTO request
    ) {
        try {
            String itensJson = objectMapper.writeValueAsString(request.itens());
            Carrinho carrinho = carrinhoUseCases.salvarCarrinho(userId, itensJson);

            CarrinhoResponseDTO response = new CarrinhoResponseDTO();
            response.setItens(request.itens());
            response.setDataUltimaAtualizacao(carrinho.getDataUltimaAtualizacao());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Limpar carrinho do usuário", description = "Remove todos os itens do carrinho do usuário logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Carrinho limpo com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content())
    })
    @DeleteMapping
    public ResponseEntity<Void> limparCarrinho(
            @RequestHeader("X-User-Id") Integer userId
    ) {
        try {
            carrinhoUseCases.limparCarrinho(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}