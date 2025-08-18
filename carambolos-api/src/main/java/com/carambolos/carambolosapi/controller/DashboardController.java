package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard Controller", description = "Consulta endpoints de bolos, fornadas e clientes")
@SecurityRequirement(name = "Bearer")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(summary = "Conta a quantidade de clientes únicos que fizeram pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem da quantidade de clientes realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum clientes encontrados")
    })
    @GetMapping("/qtdClientes")
    public ResponseEntity<Long> countClientes() {
        long qtdClientes = dashboardService.qtdClientesUnicos();
        if (qtdClientes == 0) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok().body(qtdClientes);
    }

    @GetMapping("/qtdPedidosConcluidos")
    public ResponseEntity<Long> countPedidosConcluidos() {
        long qtd = dashboardService.countPedidosByStatusConcluido();
        return ResponseEntity.ok(qtd);
    }

    @GetMapping("/qtdPedidosAbertos")
    public ResponseEntity<Long> countPedidosAbertos() {
        long qtd = dashboardService.countPedidosAbertos();
        return ResponseEntity.ok(qtd);
    }

    @GetMapping("/qtdPedidosTotal")
    public ResponseEntity<Long> countPedidosTotal() {
        long qtd = dashboardService.countPedidosTotal();
        return ResponseEntity.ok(qtd);
    }

    @Operation(summary = "Lista os bolos mais pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem dos bolos mais pedidos realizada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum bolo encontrado")
    })
    @GetMapping("/bolosMaisPedidos")
    public ResponseEntity<List<Map<String, Object>>> getBolosMaisPedidos(
            @RequestParam(defaultValue = "5") int limit) {
        List<Map<String, Object>> bolosMaisPedidos = dashboardService.getBolosMaisPedidos(limit);
        if (bolosMaisPedidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(bolosMaisPedidos);
    }


}
