package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/qtdClientesUnicos")
    public ResponseEntity<Long> countClientes() {
        long qtdClientes = dashboardService.qtdClientesUnicos();
        if (qtdClientes == 0) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok().body(qtdClientes);
    }

    @Operation(summary = "Conta a quantidade de pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contagem da quantidade de pedidos")
    })
    @GetMapping("/qtdPedidos")
    public ResponseEntity<Map<String, Long>> countPedidosTotal() {
        Map<String, Long> qtd = dashboardService.countPedidos();
        return ResponseEntity.ok(qtd);
    }

    @Operation(summary = "Lista os bolos mais pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem dos bolos mais pedidos realizada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum bolo encontrado")
    })
    @GetMapping("/bolosMaisPedidos")
    public ResponseEntity<List<Map<String, Object>>> getBolosMaisPedidos() {
        List<Map<String, Object>> bolosMaisPedidos = dashboardService.getBolosMaisPedidos();
        if (bolosMaisPedidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(bolosMaisPedidos);
    }

    @Operation(summary = "Lista os produtos de fornada mais pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem dos produtos de fornadas mais pedidos realizada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum produto encontrado")
    })
    @GetMapping("/produtosFornadasMaisPedidos")
    public ResponseEntity<List<Map<String, Object>>> getFornadasMaisPedidas() {
        List<Map<String, Object>> fornadasMaisPedidas = dashboardService.getFornadasMaisPedidas();
        if (fornadasMaisPedidas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(fornadasMaisPedidas);
    }

    @Operation(summary = "Lista os produtos mais pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem dos produtos mais pedidos com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum produto encontrado")
    })
    @GetMapping("/produtosMaisPedidos")
    public ResponseEntity<List<Map<String, Object>>> getProdutosMaisPedidos() {
        List<Map<String, Object>> produtosMaisPedidos = dashboardService.getProdutosMaisPedidos();
        if (produtosMaisPedidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(produtosMaisPedidos);
    }

    @Operation(summary = "Lista os últimos pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem dos últimos pedidos"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    })
    @GetMapping("/ultimosPedidos")
    public ResponseEntity<List<Map<String, Object>>> getUltimosPedidos() {
        List<Map<String, Object>> ultimosPedidos = dashboardService.getUltimosPedidos();
        if (ultimosPedidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(ultimosPedidos);
    }

    @Operation(summary = "Conta as quantidades de pedidos de bolos com os status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade dos pedidos de bolos"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    })
    @GetMapping("/qtdPedidosBolo")
    public ResponseEntity<Map<String, Long>> countPedidosBolos() {
        Map<String, Long> qtdPedidosBolo = dashboardService.countPedidosBolos();
        if (qtdPedidosBolo.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(qtdPedidosBolo);
    }

    @Operation(summary = "Conta as quantidades de pedidos de fornadas com os status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade dos pedidos de fornadas"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    })
    @GetMapping("/qtdPedidosFornada")
    public ResponseEntity<Map<String, Long>> countPedidosFornada() {
        Map<String, Long> qtdPedidosFornada = dashboardService.countPedidosFornada();
        if (qtdPedidosFornada.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(qtdPedidosFornada);
    }

    @Operation(summary = "Conta as quantidades de pedidos de fornadas com os status concluído e cancelado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade dos pedidos de fornadas concluídos e cancelados"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    })
    @GetMapping("/qtdPedidosFornadaPorPeriodo")
    public ResponseEntity<Map<String, Map<String, Long>>> countPedidosFornadaPorPeriodo(
            @RequestParam String periodo
    ) {
        Map<String, Map<String, Long>> qtdPedidosFornadaConcluidosECancelados = dashboardService.countPedidosFornadaPorPeriodo(periodo);
        if (qtdPedidosFornadaConcluidosECancelados.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(qtdPedidosFornadaConcluidosECancelados);
    }

    @Operation(summary = "Conta as quantidades de pedidos de bolos com os status concluído e cancelado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade dos pedidos de bolos concluídos e cancelados"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    })
    @GetMapping("/qtdPedidosBoloPorPeriodo")
    public ResponseEntity<Map<String, Map<String, Long>>> countPedidosBolosPorPeriodo(
            @RequestParam String periodo
    ) {
        Map<String, Map<String, Long>> qtdPedidosBoloConcluidosECancelados = dashboardService.countPedidosBolosPorPeriodo(periodo);
        if (qtdPedidosBoloConcluidosECancelados.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(qtdPedidosBoloConcluidosECancelados);
    }

    // KPIs específicos para fornadas
    @Operation(summary = "KPI de uma fornada específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "KPI da fornada retornado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum dado encontrado")
    })
    @GetMapping("/kpi-fornada/{fornadaId}")
    public ResponseEntity<Map<String, Object>> getKPIFornada(@PathVariable Integer fornadaId) {
        Map<String, Object> kpi = dashboardService.getKPIFornada(fornadaId);
        if (kpi.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(kpi);
    }

    @Operation(summary = "KPI da fornada mais recente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "KPI da fornada mais recente retornado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum dado encontrado")
    })
    @GetMapping("/kpi-fornada-mais-recente")
    public ResponseEntity<Map<String, Object>> getKPIFornadaMaisRecente() {
        Map<String, Object> kpi = dashboardService.getKPIFornadaMaisRecente();
        if (kpi.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(kpi);
    }

    @Operation(summary = "KPI de fornadas por mês/ano")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "KPI das fornadas do período retornado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum dado encontrado")
    })
    @GetMapping("/kpi-fornadas-por-periodo")
    public ResponseEntity<Map<String, Object>> getKPIFornadasPorPeriodo(
            @RequestParam Integer ano,
            @RequestParam Integer mes
    ) {
        Map<String, Object> kpi = dashboardService.getKPIFornadasPorMesAno(ano, mes);
        if (kpi.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(kpi);
    }

}
