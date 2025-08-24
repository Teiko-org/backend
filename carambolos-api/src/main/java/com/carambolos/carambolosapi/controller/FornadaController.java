package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.controller.request.*;
import com.carambolos.carambolosapi.controller.response.DetalhePedidoBoloDTO;
import com.carambolos.carambolosapi.controller.response.DetalhePedidoFornadaDTO;
import com.carambolos.carambolosapi.controller.response.ProdutoFornadaDaVezResponse;
import com.carambolos.carambolosapi.controller.response.MesAnoResponse;
import com.carambolos.carambolosapi.controller.response.FornadaComItensResponse;
import com.carambolos.carambolosapi.controller.response.ProdutoFornadaResponseDTO;
import com.carambolos.carambolosapi.model.Fornada;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.PedidoFornada;
import com.carambolos.carambolosapi.model.ProdutoFornada;
import com.carambolos.carambolosapi.model.projection.ProdutoFornadaDaVezProjection;
import com.carambolos.carambolosapi.service.FornadaDaVezService;
import com.carambolos.carambolosapi.service.FornadaService;
import com.carambolos.carambolosapi.service.PedidoFornadaService;
import com.carambolos.carambolosapi.service.ProdutoFornadaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fornadas")
@Tag(name = "Fornada Controller", description = "Gerencia Fornadas, Produtos da Fornada, Fornadas da Vez e Pedidos de Fornada")
@SecurityRequirement(name = "Bearer")
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

    @Operation(summary = "Lista meses/anos que tiveram fornadas (para filtro)")
    @GetMapping("/meses-anos")
    public ResponseEntity<List<MesAnoResponse>> listarMesesAnosDisponiveis() {
        var proj = fornadaService
                .listarFornada() // obter todas e derivar mês/ano distintos em memória caso native indisponível via service
                .stream()
                .filter(Fornada::isAtivo)
                .map(f -> new MesAnoResponse(f.getDataInicio().getYear(), f.getDataInicio().getMonthValue()))
                .distinct()
                .sorted((a, b) -> {
                    int byYear = b.ano().compareTo(a.ano());
                    if (byYear != 0) return byYear;
                    return b.mes().compareTo(a.mes());
                })
                .toList();
        return ResponseEntity.ok(proj);
    }

    @Operation(summary = "Lista fornadas com itens por mês/ano")
    @GetMapping("/com-itens")
    public ResponseEntity<List<FornadaComItensResponse>> listarFornadasComItensPorMesAno(
            @RequestParam("ano") Integer ano,
            @RequestParam("mes") Integer mes
    ) {
        var fornadas = fornadaService.listarFornadasPorMesAno(ano, mes);
        List<FornadaComItensResponse> resposta = new ArrayList<>();
        for (Fornada f : fornadas) {
            var itens = fornadaDaVezService.buscarProdutosPorFornadaId(f.getId());
            resposta.add(FornadaComItensResponse.of(f, itens));
        }
        return ResponseEntity.ok(resposta);
    }

    @Operation(summary = "Lista produtos da fornada mais recente (sem imagens)")
    @GetMapping("/mais-recente/produtos")
    public ResponseEntity<List<ProdutoFornadaDaVezResponse>> listarProdutosDaFornadaMaisRecente() {
        var fornadaOpt = fornadaService.buscarFornadaMaisRecente();
        if (fornadaOpt.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        var itens = fornadaDaVezService.buscarProdutosPorFornadaId(fornadaOpt.get().getId());
        return ResponseEntity.ok(ProdutoFornadaDaVezResponse.toProdutoFornadaDaVezResonse(itens));
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

    @PostMapping(value = "/produto-fornada", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Cria um novo produto da fornada com imagens",
            description = "Cadastra um novo produto da fornada e faz upload de imagens associadas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto da fornada criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoFornadaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
    })
    public ResponseEntity<ProdutoFornadaResponseDTO> criarProdutoFornada(
            @RequestPart("produto") String produto,
            @RequestPart("descricao") String descricao,
            @RequestPart("valor") String valor,
            @RequestPart("categoria") String categoria,
            @RequestPart("imagens") MultipartFile[] imagens
    ) {
        Double valorDouble = Double.valueOf(valor);
        ProdutoFornada produtoFornada = produtoFornadaService.criarProdutoFornada(produto, descricao, valorDouble, categoria, imagens);
        return ResponseEntity.status(201).body(ProdutoFornadaResponseDTO.fromEntity(produtoFornada));
    }

    @Operation(summary = "Lista todos os produtos da fornada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem dos produtos da fornada realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoFornada.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado", content = @Content())
    })
    @GetMapping("/produto-fornada")
    public ResponseEntity<List<ProdutoFornada>> listarProdutoFornada(
            @RequestParam(defaultValue = "") List<String> categorias
    ) {
        return ResponseEntity.status(200).body(produtoFornadaService.listarProdutosFornada(categorias));
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

    @Operation(summary = "Atualiza o status de um produto fornada", description = "Atualiza o status de um produto fornada")
    @PatchMapping("/produto-fornada/status/{id}")
    public ResponseEntity<Void> atualizarStatusProdutoFornada(
            @RequestBody StatusRequestDTO status,
            @PathVariable Integer id
    ) {
        produtoFornadaService.atualizarStatusProdutoFornada(status.isAtivo(), id);
        return ResponseEntity.ok().build();
    }

    // ----------------- FORNADA DA VEZ -----------------

    @Operation(summary = "Cria uma nova fornada da vez")
    @PostMapping("/da-vez")
    public ResponseEntity<FornadaDaVez> criarFornadaDaVez(@RequestBody @Valid FornadaDaVezRequestDTO request) {
        return ResponseEntity.status(201).body(fornadaDaVezService.criarFornadaDaVez(request));
    }

    @Operation(summary = "Lista todas as fornadas da vez")
    @GetMapping("/da-vez")
    public ResponseEntity<List<FornadaDaVez>> listarFornadasDaVez() {
        return ResponseEntity.status(200).body(fornadaDaVezService.listarFornadasDaVez());
    }

    @Operation(summary = "Busca uma fornada da vez por ID")
    @GetMapping("/da-vez/{id}")
    public ResponseEntity<FornadaDaVez> buscarFornadaDaVez(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(fornadaDaVezService.buscarFornadaDaVez(id));
    }

    @Operation(summary = "Atualiza a quantidade da fornada da vez")
    @PutMapping("/da-vez/{id}")
    public ResponseEntity<FornadaDaVez> atualizarFornadaDaVez(
            @PathVariable Integer id,
            @RequestBody @Valid FornadaDaVezUpdateRequestDTO request) {
        return ResponseEntity.status(200).body(fornadaDaVezService.atualizarQuantidade(id, request));
    }

    @Operation(summary = "Exclui uma fornada da vez por ID")
    @DeleteMapping("/da-vez/{id}")
    public ResponseEntity<Void> excluirFornadaDaVez(@PathVariable Integer id) {
        fornadaDaVezService.excluirFornadaDaVez(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Lista produtos de uma fornada")
    @GetMapping("/da-vez/produtos")
    public ResponseEntity<List<ProdutoFornadaDaVezResponse>> buscarProdutosFornadaDaVez(
            @RequestParam("data_inicio") LocalDate dataInicio,
            @RequestParam("data_fim") LocalDate dataFim
    ) {
        List<ProdutoFornadaDaVezProjection> projections = fornadaDaVezService.buscarProdutosFornadaDaVez(dataInicio, dataFim);
        List<ProdutoFornadaDaVezResponse> response = ProdutoFornadaDaVezResponse.toProdutoFornadaDaVezResonse(projections);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista produtos de uma fornada por ID")
    @GetMapping("/da-vez/produtos/{fornadaId}")
    public ResponseEntity<List<ProdutoFornadaDaVezResponse>> buscarProdutosPorFornadaId(
            @PathVariable Integer fornadaId
    ) {
        List<ProdutoFornadaDaVezProjection> projections = fornadaDaVezService.buscarProdutosPorFornadaId(fornadaId);
        List<ProdutoFornadaDaVezResponse> response = ProdutoFornadaDaVezResponse.toProdutoFornadaDaVezResonse(projections);
        return ResponseEntity.ok(response);
    }

    // ----------------- PEDIDO FORNADA -----------------

    @Operation(summary = "Cria um novo pedido de fornada")
    @PostMapping("/pedidos")
    public ResponseEntity<PedidoFornada> criarPedidoFornada(
            @RequestBody @Valid PedidoFornadaRequestDTO request
    ) {
        return ResponseEntity.status(201).body(pedidoFornadaService.criarPedidoFornada(request));
    }

    @Operation(summary = "Lista todos os pedidos de fornada")
    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoFornada>> listarPedidos() {
        return ResponseEntity.status(200).body(pedidoFornadaService.listarPedidosFornada());
    }

    @Operation(summary = "Busca um pedido de fornada por ID")
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoFornada> buscarPedido(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(pedidoFornadaService.buscarPedidoFornada(id));
    }

    @Operation(summary = "Atualiza um pedido de fornada existente")
    @PutMapping("/pedidos/{id}")
    public ResponseEntity<PedidoFornada> atualizarPedidoFornada(
            @PathVariable Integer id,
            @RequestBody @Valid PedidoFornadaUpdateRequestDTO request) {
        return ResponseEntity.status(200).body(pedidoFornadaService.atualizarPedidoFornada(id, request));
    }

    @Operation(summary = "Exclui um pedido de fornada por ID")
    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<Void> excluirPedidoFornada(@PathVariable Integer id) {
        pedidoFornadaService.excluirPedidoFornada(id);
        return ResponseEntity.status(204).build();
    }
}