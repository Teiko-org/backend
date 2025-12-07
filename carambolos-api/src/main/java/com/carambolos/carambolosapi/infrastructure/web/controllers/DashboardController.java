package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.DashboardUseCase;
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
@SuppressWarnings("unused")
public class DashboardController {

    private final DashboardUseCase dashboardUseCase;

    public DashboardController(DashboardUseCase dashboardUseCase) {
        this.dashboardUseCase = dashboardUseCase;
    }

    @Operation(summary = "Conta a quantidade de clientes únicos que fizeram pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem da quantidade de clientes realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum clientes encontrados")
    })
    @GetMapping("/qtdClientesUnicos")
    public ResponseEntity<Long> countClientes() {
        long qtdClientes = dashboardUseCase.qtdClientesUnicos();
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
        Map<String, Long> qtd = dashboardUseCase.countPedidos();
        return ResponseEntity.ok(qtd);
    }

    @Operation(summary = "Lista os bolos mais pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem dos bolos mais pedidos realizada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum bolo encontrado")
    })
    @GetMapping("/bolosMaisPedidos")
    public ResponseEntity<List<Map<String, Object>>> getBolosMaisPedidos() {
        List<Map<String, Object>> bolosMaisPedidos = dashboardUseCase.getBolosMaisPedidos();
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
        List<Map<String, Object>> fornadasMaisPedidas = dashboardUseCase.getFornadasMaisPedidas();
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
    @SuppressWarnings("deprecation")
    public ResponseEntity<List<Map<String, Object>>> getProdutosMaisPedidos() {
        List<Map<String, Object>> produtosMaisPedidos = dashboardUseCase.getProdutosMaisPedidos();
        if (produtosMaisPedidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(produtosMaisPedidos);
    }

    @Operation(summary = "Lista os produtos cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem dos produtos cadastrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum produto encontrado")
    })
    @GetMapping("/produtosCadastrados")
    public ResponseEntity<List<Map<String, Object>>> getProdutosCadastrados() {
        List<Map<String, Object>> produtosCadastrados = dashboardUseCase.getProdutosCadastrados();
        if (produtosCadastrados.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(produtosCadastrados);
    }

    @Operation(summary = "Lista os últimos pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem dos últimos pedidos"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    })
    @GetMapping("/ultimosPedidos")
    public ResponseEntity<List<Map<String, Object>>> getUltimosPedidos() {
        List<Map<String, Object>> ultimosPedidos = dashboardUseCase.getUltimosPedidos();
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
        Map<String, Long> qtdPedidosBolo = dashboardUseCase.countPedidosBolos();
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
        Map<String, Long> qtdPedidosFornada = dashboardUseCase.countPedidosFornada();
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
        Map<String, Map<String, Long>> qtdPedidosFornadaConcluidosECancelados = dashboardUseCase.countPedidosFornadaPorPeriodo(periodo);
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
        Map<String, Map<String, Long>> qtdPedidosBoloConcluidosECancelados = dashboardUseCase.countPedidosBolosPorPeriodo(periodo);
        if (qtdPedidosBoloConcluidosECancelados.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(qtdPedidosBoloConcluidosECancelados);
    }

    @Operation(summary = "KPI de uma fornada específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "KPI da fornada retornado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum dado encontrado")
    })
    @GetMapping("/kpi-fornada/{fornadaId}")
    public ResponseEntity<Map<String, Object>> getKPIFornada(@PathVariable Integer fornadaId) {
        Map<String, Object> kpi = dashboardUseCase.getKPIFornada(fornadaId);
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
    @GetMapping(value = "/kpi-fornada-mais-recente", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getKPIFornadaMaisRecente() {
        Map<String, Object> kpi = dashboardUseCase.getKPIFornadaMaisRecente();
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
        Map<String, Object> kpi = dashboardUseCase.getKPIFornadasPorMesAno(ano, mes);
        if (kpi.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(kpi);
    }

    @Operation(summary = "Lista massas pendentes", description = "Retorna a contagem de pedidos pendentes agrupados por massa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de massas pendentes retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhuma massa pendente encontrada")
    })
    @GetMapping("/massas-pendentes")
    public ResponseEntity<List<Map<String, Object>>> getMassasPendentes() {
        List<Map<String, Object>> massasPendentes = dashboardUseCase.getMassasPendentes();
        if (massasPendentes.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(massasPendentes);
    }

    @Operation(summary = "Lista recheios pendentes", description = "Retorna a contagem de pedidos pendentes agrupados por recheio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de recheios pendentes retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum recheio pendente encontrado")
    })
    @GetMapping("/recheios-pendentes")
    public ResponseEntity<List<Map<String, Object>>> getRecheiosPendentes() {
        List<Map<String, Object>> recheiosPendentes = dashboardUseCase.getRecheiosPendentes();
        if (recheiosPendentes.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(recheiosPendentes);
    }

    @Operation(summary = "Lista pedidos próximos da data de entrega", 
              description = "Retorna pedidos cuja data de entrega está nos próximos N dias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    })
    @GetMapping("/pedidos-proximos-entrega")
    public ResponseEntity<List<Map<String, Object>>> getPedidosProximosDaEntrega(
            @RequestParam(defaultValue = "7") int diasProximos
    ) {
        List<Map<String, Object>> pedidos = dashboardUseCase.getPedidosProximosDaEntrega(diasProximos);
        if (pedidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(pedidos);
    }

    @Operation(summary = "Lista itens mais pedidos por período", 
              description = "Retorna massas, recheios ou decorações mais pedidas por semana, mês ou ano")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum item encontrado")
    })
    @GetMapping("/itens-mais-pedidos-por-periodo")
    public ResponseEntity<List<Map<String, Object>>> getItensMaisPedidosPorPeriodo(
            @RequestParam String tipoItem,
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Integer mes
    ) {
        List<Map<String, Object>> itens = dashboardUseCase.getItensMaisPedidosPorPeriodo(tipoItem, periodo, ano, mes);
        if (itens.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(itens);
    }

}
