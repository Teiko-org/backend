package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.infrastructure.web.response.ProdutoFornadaDaVezResponse;
import com.carambolos.carambolosapi.infrastructure.web.response.MesAnoResponse;
import com.carambolos.carambolosapi.infrastructure.web.response.FornadaComItensResponse;
import com.carambolos.carambolosapi.infrastructure.web.response.ProdutoFornadaResponseDTO;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.Fornada;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada;
 
import com.carambolos.carambolosapi.domain.entity.ProdutoFornada;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.ProdutoFornadaDaVezProjection;
import com.carambolos.carambolosapi.application.usecases.FornadaDaVezUseCases;
import com.carambolos.carambolosapi.application.usecases.FornadasUseCases;
import com.carambolos.carambolosapi.application.usecases.PedidoFornadaUseCases;
import com.carambolos.carambolosapi.application.usecases.ProdutoFornadaUseCases;
import com.carambolos.carambolosapi.infrastructure.web.request.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@SuppressWarnings("unused")
public class FornadaController {

    @Autowired
    private final FornadaDaVezUseCases fornadaDaVezService;

    private final FornadasUseCases fornadasUseCases;
    private final PedidoFornadaUseCases pedidoFornadaUseCases;

    @Autowired
    private final ProdutoFornadaUseCases produtoFornadaService;

    public FornadaController(FornadaDaVezUseCases fornadaDaVezService, ProdutoFornadaUseCases produtoFornadaService, FornadasUseCases fornadasUseCases, PedidoFornadaUseCases pedidoFornadaUseCases) {
        this.fornadaDaVezService = fornadaDaVezService;
        this.produtoFornadaService = produtoFornadaService;
        this.fornadasUseCases = fornadasUseCases;
        this.pedidoFornadaUseCases = pedidoFornadaUseCases;
    }

    // ------------ FORNADA -----------------

    @Operation(summary = "Cria uma nova fornada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fornada criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<Fornada> criarFornada(@RequestBody @Valid FornadaRequestDTO request) {
        var created = fornadasUseCases.criar(request.id(), request.dataInicio(), request.dataFim());
        // manter resposta como Entity JPA no contrato atual
        var response = new Fornada();
        response.setId(created.getId());
        response.setDataInicio(created.getDataInicio());
        response.setDataFim(created.getDataFim());
        response.setAtivo(Boolean.TRUE.equals(created.getAtivo()));
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Lista todas as fornadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem das fornadas realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma fornada encontrada")
    })
    @GetMapping
    public ResponseEntity<List<Fornada>> listarFornadas() {
        var list = fornadasUseCases.listarAtivas().stream().map(d -> {
            var e = new Fornada();
            e.setId(d.getId()); e.setDataInicio(d.getDataInicio()); e.setDataFim(d.getDataFim()); e.setAtivo(Boolean.TRUE.equals(d.getAtivo()));
            return e;
        }).toList();
        
        return ResponseEntity.status(200).body(list);
    }

    @Operation(summary = "Lista todas as fornadas (ativas e encerradas)")
    @GetMapping("/todas")
    public ResponseEntity<List<Fornada>> listarTodasFornadas() {
        var list = fornadasUseCases.listarTodas().stream().map(d -> {
            var e = new Fornada();
            e.setId(d.getId()); e.setDataInicio(d.getDataInicio()); e.setDataFim(d.getDataFim()); e.setAtivo(Boolean.TRUE.equals(d.getAtivo()));
            return e;
        }).toList();
        
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Lista meses/anos que tiveram fornadas (para filtro)")
    @GetMapping("/meses-anos")
    public ResponseEntity<List<MesAnoResponse>> listarMesesAnosDisponiveis() {
        var proj = fornadasUseCases
                .listarTodas() // incluir ativas e encerradas para o filtro
                .stream()
                .map(d -> new MesAnoResponse(d.getDataInicio().getYear(), d.getDataInicio().getMonthValue()))
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
        var fornadas = fornadasUseCases.listarPorMesAno(ano, mes).stream().map(d -> {
            var e = new Fornada();
            e.setId(d.getId()); e.setDataInicio(d.getDataInicio()); e.setDataFim(d.getDataFim()); e.setAtivo(Boolean.TRUE.equals(d.getAtivo()));
            return e;
        }).toList();
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
        var fornadaList = fornadasUseCases.buscarMaisRecente();
        if (fornadaList.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        var d = fornadaList.get(0);
        var e = new Fornada();
        e.setId(d.getId());
        e.setDataInicio(d.getDataInicio());
        e.setDataFim(d.getDataFim());
        e.setAtivo(Boolean.TRUE.equals(d.getAtivo()));
        var itens = fornadaDaVezService.buscarProdutosPorFornadaId(e.getId());
        return ResponseEntity.ok(ProdutoFornadaDaVezResponse.toProdutoFornadaDaVezResonse(itens));
    }

    @Operation(summary = "Busca a próxima fornada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Próxima fornada encontrada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhuma fornada futura encontrada")
    })
    @GetMapping("/proxima")
    public ResponseEntity<Fornada> buscarProximaFornada() {
        var fornadaOpt = fornadasUseCases.buscarProxima().map(d -> {
            var e = new Fornada();
            e.setId(d.getId()); e.setDataInicio(d.getDataInicio()); e.setDataFim(d.getDataFim()); e.setAtivo(Boolean.TRUE.equals(d.getAtivo()));
            return e;
        });
        if (fornadaOpt.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(fornadaOpt.get());
    }

    @Operation(summary = "Busca uma fornada por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornada encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fornada não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Fornada> buscarFornada(@PathVariable Integer id) {
        var d = fornadasUseCases.buscarPorId(id);
        var e = new Fornada();
        e.setId(d.getId()); e.setDataInicio(d.getDataInicio()); e.setDataFim(d.getDataFim()); e.setAtivo(Boolean.TRUE.equals(d.getAtivo()));
        return ResponseEntity.status(200).body(e);
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
        var updated = fornadasUseCases.atualizar(id, request.dataInicio(), request.dataFim());
        var fornada = new Fornada();
        fornada.setId(updated.getId());
        fornada.setDataInicio(updated.getDataInicio());
        fornada.setDataFim(updated.getDataFim());
        fornada.setAtivo(Boolean.TRUE.equals(updated.getAtivo()));
        return ResponseEntity.status(200).body(fornada);
    }

    @Operation(summary = "Exclui uma fornada por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fornada excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fornada não encontrada", content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirFornada(@PathVariable Integer id) {
        fornadasUseCases.encerrar(id);
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
    public ResponseEntity<Page<ProdutoFornada>> listarProdutoFornada(
            @RequestParam(defaultValue = "") List<String> categorias,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(200).body(produtoFornadaService.listarProdutosFornada(pageable, categorias));
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
        var d = pedidoFornadaUseCases.criar(request);
        return ResponseEntity.status(201).body(com.carambolos.carambolosapi.infrastructure.gateways.mapper.FornadasMapper.toEntity(d));
    }

    @Operation(summary = "Lista todos os pedidos de fornada")
    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoFornada>> listarPedidos() {
        var list = pedidoFornadaUseCases.listar().stream()
                .map(com.carambolos.carambolosapi.infrastructure.gateways.mapper.FornadasMapper::toEntity)
                .toList();
        return ResponseEntity.status(200).body(list);
    }

    @Operation(summary = "Busca um pedido de fornada por ID")
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoFornada> buscarPedido(@PathVariable Integer id) {
        var d = pedidoFornadaUseCases.buscarPorId(id);
        return ResponseEntity.status(200).body(com.carambolos.carambolosapi.infrastructure.gateways.mapper.FornadasMapper.toEntity(d));
    }

    @Operation(summary = "Atualiza um pedido de fornada existente")
    @PutMapping("/pedidos/{id}")
    public ResponseEntity<PedidoFornada> atualizarPedidoFornada(
            @PathVariable Integer id,
            @RequestBody @Valid PedidoFornadaUpdateRequestDTO request) {
        var d = pedidoFornadaUseCases.atualizar(id, request);
        return ResponseEntity.status(200).body(com.carambolos.carambolosapi.infrastructure.gateways.mapper.FornadasMapper.toEntity(d));
    }

    @Operation(summary = "Exclui um pedido de fornada por ID")
    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<Void> excluirPedidoFornada(@PathVariable Integer id) {
        pedidoFornadaUseCases.excluir(id);
        return ResponseEntity.status(204).build();
    }
}