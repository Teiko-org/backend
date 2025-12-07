package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.ProducaoService;
import com.carambolos.carambolosapi.infrastructure.web.response.PedidosPendentesPorMassaResponse;
import com.carambolos.carambolosapi.infrastructure.web.response.PedidosPendentesPorRecheioResponse;
import com.carambolos.carambolosapi.infrastructure.web.response.MassasMaisPedidasPorMesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producao")
@Tag(name = "Produção Controller", description = "Endpoints otimizados para a tela de produção")
@SecurityRequirement(name = "Bearer")
public class ProducaoController {

    private final ProducaoService producaoService;

    public ProducaoController(ProducaoService producaoService) {
        this.producaoService = producaoService;
    }

    @Operation(summary = "Busca pedidos pendentes agrupados por massa", 
               description = "Retorna lista de massas com quantidade de pedidos pendentes e IDs dos pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido pendente encontrado")
    })
    @GetMapping("/pedidos-pendentes-por-massa")
    public ResponseEntity<List<PedidosPendentesPorMassaResponse>> getPedidosPendentesPorMassa() {
        List<PedidosPendentesPorMassaResponse> result = producaoService.getPedidosPendentesPorMassa();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Busca pedidos pendentes agrupados por recheio", 
               description = "Retorna lista de recheios com quantidade de pedidos pendentes e IDs dos pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido pendente encontrado")
    })
    @GetMapping("/pedidos-pendentes-por-recheio")
    public ResponseEntity<List<PedidosPendentesPorRecheioResponse>> getPedidosPendentesPorRecheio() {
        List<PedidosPendentesPorRecheioResponse> result = producaoService.getPedidosPendentesPorRecheio();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Busca massas mais pedidas por mês", 
               description = "Retorna dados agregados de massas mais pedidas por mês para gráfico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso")
    })
    @GetMapping("/massas-mais-pedidas-por-mes")
    public ResponseEntity<MassasMaisPedidasPorMesResponse> getMassasMaisPedidasPorMes(
            @RequestParam(defaultValue = "2025") int ano) {
        return ResponseEntity.ok(producaoService.getMassasMaisPedidasPorMes(ano));
    }
}
