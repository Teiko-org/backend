package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.controller.dto.FornadaDaVezRequestDTO;
import com.carambolos.carambolosapi.controller.dto.FornadaRequestDTO;
import com.carambolos.carambolosapi.controller.dto.ProdutoFornadaRequestDTO;
import com.carambolos.carambolosapi.model.Fornada;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.ProdutoFornada;
import com.carambolos.carambolosapi.service.FornadaDaVezService;
import com.carambolos.carambolosapi.service.FornadaService;
import com.carambolos.carambolosapi.service.PedidoFornadaService;
import com.carambolos.carambolosapi.service.ProdutoFornadaService;
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
@RequestMapping("/fornadas")
@Tag(name = "Fornada Controller", description = "Gerencia Fornadas, Produtos da Fornada, Fornadas da Vez e Pedidos de Fornada")
public class FornadaController {

    @Autowired
    private final FornadaService fornadaService;

    @Autowired
    private final FornadaDaVezService fornadaDaVezService;

    @Autowired
    private final PedidoFornadaService pedidoFornadaService;

    @Autowired
    private final ProdutoFornadaService produtoFornadaService;

    public FornadaController(FornadaService fornadaService, FornadaDaVezService fornadaDaVezService, PedidoFornadaService pedidoFornadaService, ProdutoFornadaService produtoFornadaService) {
        this.fornadaService = fornadaService;
        this.fornadaDaVezService = fornadaDaVezService;
        this.pedidoFornadaService = pedidoFornadaService;
        this.produtoFornadaService = produtoFornadaService;
    }

    // ------------ FORNADA -----------------

    @Operation(summary = "Cria uma nova fornada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fornada criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<Fornada> criarFornada(@RequestBody @Valid FornadaRequestDTO request) {
        return ResponseEntity.status(201).body(fornadaService.criarFornada(request));
    }

    @Operation(summary = "Lista todas as fornadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem das fornadas realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma fornada encontrada")
    })
    @GetMapping
    public ResponseEntity<List<Fornada>> listarFornadas() {
        return ResponseEntity.status(200).body(fornadaService.listarFornada());
    }

    @Operation(summary = "Busca uma fornada por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornada encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fornada não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Fornada> buscarFornada(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(fornadaService.buscarFornada(id));
    }

    @Operation(summary = "Atualiza uma fornada existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornada atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Fornada.class))),
            @ApiResponse(responseCode = "404", description = "Fornada não encontrada", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content())
    })
    @PutMapping("/{id}")
    public ResponseEntity<Fornada> atualizarFornada(@PathVariable Integer id, @RequestBody @Valid FornadaRequestDTO request) {
        fornadaService.atualizarFornada(id, request);
        Fornada fornada = request.toEntity();
        return ResponseEntity.status(200).body(fornada);
    }

    @Operation(summary = "Exclui uma fornada por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fornada excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fornada não encontrada", content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirFornada(@PathVariable Integer id) {
        fornadaService.excluirFornada(id);
        return ResponseEntity.status(204).build();
    }

    // ---------------- PRODUTO DA FORNADA -------------

    @Operation(summary = "Cria um novo produto da fornada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto da fornada criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoFornada.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content())
    })
    @PostMapping("/produto-fornada")
    public ResponseEntity<ProdutoFornada> criarProdutoFornada(
            @RequestBody @Valid ProdutoFornadaRequestDTO request
    ){
        return ResponseEntity.status(201).body(produtoFornadaService.criarProdutoFornada(request));
    }

    @Operation(summary = "Lista todos os produtos da fornada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem dos produtos da fornada realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoFornada.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado", content = @Content())
    })
    @GetMapping("/produto-fornada")
    public ResponseEntity<List<ProdutoFornada>> listarProdutoFornada() {
        return ResponseEntity.status(200).body(produtoFornadaService.listarProdutosFornada());
    }

    @Operation(summary = "Busca um produto da fornada por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoFornada.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content())
    })
    @GetMapping("/produto-fornada/{id}")
    public ResponseEntity<ProdutoFornada> buscarProdutoFornada(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(produtoFornadaService.buscarProdutoFornada(id));
    }

    @Operation(summary = "Atualiza um produto da fornada existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto da fornada atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoFornada.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content())
    })
    @PutMapping("/produto-fornada/{id}")
    public ResponseEntity<ProdutoFornada> atualizarProdutoFornada(
            @PathVariable Integer id,
            @RequestBody @Valid ProdutoFornadaRequestDTO request) {
        return ResponseEntity.status(200).body(produtoFornadaService.atualizarProdutoFornada(id, request));
    }

    @Operation(summary = "Exclui um produto da fornada por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto da fornada excluído com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content())
    })
    @DeleteMapping("/produto-fornada/{id}")
    public ResponseEntity<Void> excluirProdutoFornada(@PathVariable Integer id) {
        produtoFornadaService.excluirProdutoFornada(id);
        return ResponseEntity.status(204).build();
    }

    // ----------------- FORNADA DA VEZ -----------------

    @Operation(summary = "Cria uma nova fornada da vez")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fornada da vez criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FornadaDaVez.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content())
    })
    @PostMapping("/da-vez")
    public ResponseEntity<FornadaDaVez> criarFornadaDaVez(@RequestBody @Valid FornadaDaVezRequestDTO request) {
        return ResponseEntity.status(201).body(fornadaDaVezService.criarFornadaDaVez(request));
    }

    @Operation(summary = "Lista todas as fornadas da vez")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem das fornadas da vez realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FornadaDaVez.class))),
            @ApiResponse(responseCode = "404", description = "Nenhuma fornada da vez encontrada", content = @Content())
    })
    @GetMapping("/da-vez")
    public ResponseEntity<List<FornadaDaVez>> listarFornadasDaVez() {
        return ResponseEntity.status(200).body(fornadaDaVezService.listarFornadasDaVez());
    }
}
