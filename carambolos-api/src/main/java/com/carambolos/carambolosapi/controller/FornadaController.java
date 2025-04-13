package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.controller.request.*;
import com.carambolos.carambolosapi.model.Fornada;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.PedidoFornada;
import com.carambolos.carambolosapi.model.ProdutoFornada;
import com.carambolos.carambolosapi.service.FornadaDaVezService;
import com.carambolos.carambolosapi.service.FornadaService;
import com.carambolos.carambolosapi.service.PedidoFornadaService;
import com.carambolos.carambolosapi.service.ProdutoFornadaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornadas")
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

    @PostMapping
    public ResponseEntity<Fornada> criarFornada(@RequestBody @Valid FornadaRequest request) {
        return ResponseEntity.status(201).body(fornadaService.criarFornada(request));
    }

    @GetMapping
    public ResponseEntity<List<Fornada>> listarFornadas() {
        return ResponseEntity.status(200).body(fornadaService.listarFornada());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornada> buscarFornada(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(fornadaService.buscarFornada(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fornada> atualizarFornada(@PathVariable Integer id, @RequestBody @Valid FornadaRequest request) {
        fornadaService.atualizarFornada(id, request);
        Fornada fornada = request.toEntity();
        return ResponseEntity.status(200).body(fornada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirFornada(@PathVariable Integer id) {
        fornadaService.excluirFornada(id);
        return ResponseEntity.status(204).build();
    }

    // ---------------- PRODUTO DA FORNADA -------------

    @PostMapping("/produto-fornada")
    public ResponseEntity<ProdutoFornada> criarProdutoFornada(
            @RequestBody @Valid ProdutoFornadaRequest request
    ){
        return ResponseEntity.status(201).body(produtoFornadaService.criarProdutoFornada(request));
    }

    @GetMapping("/produto-fornada")
    public ResponseEntity<List<ProdutoFornada>> listarProdutoFornada() {
        return ResponseEntity.status(200).body(produtoFornadaService.listarProdutosFornada());
    }

    @GetMapping("/produto-fornada/{id}")
    public ResponseEntity<ProdutoFornada> buscarProdutoFornada(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(produtoFornadaService.buscarProdutoFornada(id));
    }

    @PutMapping("/produto-fornada/{id}")
    public ResponseEntity<ProdutoFornada> atualizarProdutoFornada(
            @PathVariable Integer id,
            @RequestBody @Valid ProdutoFornadaRequest request) {
        return ResponseEntity.status(200).body(produtoFornadaService.atualizarProdutoFornada(id, request));
    }

    @DeleteMapping("/produto-fornada/{id}")
    public ResponseEntity<Void> excluirProdutoFornada(@PathVariable Integer id) {
        produtoFornadaService.excluirProdutoFornada(id);
        return ResponseEntity.status(204).build();
    }

    // ----------------- FORNADA DA VEZ -----------------

    @PostMapping("/da-vez")
    public ResponseEntity<FornadaDaVez> criarFornadaDaVez(@RequestBody @Valid FornadaDaVezRequest request) {
        return ResponseEntity.status(201).body(fornadaDaVezService.criarFornadaDaVez(request));
    }

    @GetMapping("/da-vez")
    public ResponseEntity<List<FornadaDaVez>> listarFornadasDaVez() {
        return ResponseEntity.status(200).body(fornadaDaVezService.listarFornadasDaVez());
    }

    @GetMapping("/da-vez/{id}")
    public ResponseEntity<FornadaDaVez> buscarFornadaDaVez(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(fornadaDaVezService.buscarFornadaDaVez(id));
    }

    @PutMapping("/da-vez/{id}")
    public ResponseEntity<FornadaDaVez> atualizarFornadaDaVez(
            @PathVariable Integer id,
            @RequestBody @Valid FornadaDaVezUpdateRequest request) {
        return ResponseEntity.status(200).body(fornadaDaVezService.atualizarQuantidade(id, request));
    }

    @DeleteMapping("/da-vez/{id}")
    public ResponseEntity<Void> excluirFornadaDaVez(@PathVariable Integer id) {
        fornadaDaVezService.excluirFornadaDaVez(id);
        return ResponseEntity.status(204).build();
    }

    // ----------------- PEDIDO FORNADA -----------------

    @PostMapping("/pedidos")
    public ResponseEntity<PedidoFornada> criarPedidoFornada(
            @RequestBody @Valid PedidoFornadaRequest request
    ){
        return ResponseEntity.status(201).body(pedidoFornadaService.criarPedidoFornada(request));
    }

    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoFornada>> listarPedidos() {
        return ResponseEntity.status(200).body(pedidoFornadaService.listarPedidosFornada());
    }

    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoFornada> buscarPedido(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(pedidoFornadaService.buscarPedidoFornada(id));
    }

    @PutMapping("/pedidos/{id}")
    public ResponseEntity<PedidoFornada> atualizarPedidoFornada(
            @PathVariable Integer id,
            @RequestBody @Valid PedidoFornadaUpdateRequest request) {
        return ResponseEntity.status(200).body(pedidoFornadaService.atualizarPedidoFornada(id, request));
    }

    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<Void> excluirPedidoFornada(@PathVariable Integer id) {
        pedidoFornadaService.excluirPedidoFornada(id);
        return ResponseEntity.status(204).build();
    }
}
