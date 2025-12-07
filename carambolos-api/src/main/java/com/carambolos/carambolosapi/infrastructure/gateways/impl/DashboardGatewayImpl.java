package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.DashboardGateway;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.DecoracaoEntity;
import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.*;
import com.carambolos.carambolosapi.domain.entity.ProdutoFornada;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.Fornada;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.*;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.FornadasMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class DashboardGatewayImpl implements DashboardGateway {
    private final PedidoBoloRepository pedidoBoloRepository;
    private final BoloRepository boloRepository;
    private final PedidoFornadaRepository pedidoFornadaRepository;
    private final ProdutoFornadaRepository produtoFornadaRepository;
    private final FornadaDaVezRepository fornadaDaVezRepository;
    private final ResumoPedidoRepository resumoPedidoRepository;
    private final DecoracaoRepository decoracaoRepository;
    private final FornadaRepository fornadaRepository;
    private final MassaRepository massaRepository;
    private final RecheioPedidoRepository recheioPedidoRepository;
    private final RecheioUnitarioRepository recheioUnitarioRepository;
    private final RecheioExclusivoRepository recheioExclusivoRepository;

    public DashboardGatewayImpl(
            PedidoBoloRepository pedidoBoloRepository,
            BoloRepository boloRepository,
            PedidoFornadaRepository pedidoFornadaRepository,
            ProdutoFornadaRepository produtoFornadaRepository,
            FornadaDaVezRepository fornadaDaVezRepository,
            ResumoPedidoRepository resumoPedidoRepository,
            DecoracaoRepository decoracaoRepository,
            FornadaRepository fornadaRepository,
            MassaRepository massaRepository,
            RecheioPedidoRepository recheioPedidoRepository,
            RecheioUnitarioRepository recheioUnitarioRepository,
            RecheioExclusivoRepository recheioExclusivoRepository
    ) {
        this.pedidoBoloRepository = pedidoBoloRepository;
        this.boloRepository = boloRepository;
        this.pedidoFornadaRepository = pedidoFornadaRepository;
        this.produtoFornadaRepository = produtoFornadaRepository;
        this.fornadaDaVezRepository = fornadaDaVezRepository;
        this.resumoPedidoRepository = resumoPedidoRepository;
        this.decoracaoRepository = decoracaoRepository;
        this.fornadaRepository = fornadaRepository;
        this.massaRepository = massaRepository;
        this.recheioPedidoRepository = recheioPedidoRepository;
        this.recheioUnitarioRepository = recheioUnitarioRepository;
        this.recheioExclusivoRepository = recheioExclusivoRepository;
    }

    @Override
    public long qtdClientesUnicos() {
        List<PedidoBoloEntity> pedidosBolo = pedidoBoloRepository.findAll();
        List<PedidoFornada> pedidoFornadas = pedidoFornadaRepository.findAll();

        Set<String> clientesUnicos = new HashSet<>();

        pedidosBolo.forEach(p -> {
            if (p.getUsuarioId() != null) {
                clientesUnicos.add("U" + p.getUsuarioId());
            } else if (p.getTelefoneCliente() != null) {
                clientesUnicos.add("T" + p.getTelefoneCliente());
            }
        });

        pedidoFornadas.forEach(p -> {
            if (p.getUsuario() != null) {
                clientesUnicos.add("U" + p.getUsuario());
            } else if (p.getTelefoneCliente() != null) {
                clientesUnicos.add("T" + p.getTelefoneCliente());
            }
        });

        return clientesUnicos.size();
    }

    @Override
    public Map<String, Long> countPedidos() {
        long pedido = resumoPedidoRepository.count();
        long pedidoConcluido = resumoPedidoRepository.countByStatus(StatusEnum.CONCLUIDO);
        long pedidoPago = resumoPedidoRepository.countByStatus(StatusEnum.PAGO);
        long pedidoPendente = resumoPedidoRepository.countByStatus(StatusEnum.PENDENTE);
        long pedidoCancelado = resumoPedidoRepository.countByStatus(StatusEnum.CANCELADO);

        Map<String, Long> resultado = new HashMap<>();

        resultado.put("total", pedido);
        resultado.put("concluídos", pedidoConcluido);
        resultado.put("pagos", pedidoPago);
        resultado.put("pendentes", pedidoPendente);
        resultado.put("cancelados", pedidoCancelado);

        return resultado;
    }

    @Override
    public Map<String, Long> countPedidosBolos() {
        long pedidoBolo = resumoPedidoRepository.countByPedidoBoloIdIsNotNull();
        long pedidoBoloConcluido = resumoPedidoRepository.countByStatusAndPedidoBoloIdIsNotNull(StatusEnum.CONCLUIDO);
        long pedidoBoloPago = resumoPedidoRepository.countByStatusAndPedidoBoloIdIsNotNull(StatusEnum.PAGO);
        long pedidoBoloPendente = resumoPedidoRepository.countByStatusAndPedidoBoloIdIsNotNull(StatusEnum.PENDENTE);
        long pedidoBoloCancelado = resumoPedidoRepository.countByStatusAndPedidoBoloIdIsNotNull(StatusEnum.CANCELADO);

        Map<String, Long> resultado = new HashMap<>();

        resultado.put("total", pedidoBolo);
        resultado.put("concluídos", pedidoBoloConcluido);
        resultado.put("pagos", pedidoBoloPago);
        resultado.put("pendentes", pedidoBoloPendente);
        resultado.put("cancelados", pedidoBoloCancelado);

        return resultado;
    }

    @Override
    public Map<String, Long> countPedidosFornada() {
        long pedidoFornada = resumoPedidoRepository.countByPedidoFornadaIdIsNotNull();
        long pedidoFornadaConcluido = resumoPedidoRepository.countByStatusAndPedidoFornadaIdIsNotNull(StatusEnum.CONCLUIDO);
        long pedidoFornadaPago = resumoPedidoRepository.countByStatusAndPedidoFornadaIdIsNotNull(StatusEnum.PAGO);
        long pedidoFornadaPendente = resumoPedidoRepository.countByStatusAndPedidoFornadaIdIsNotNull(StatusEnum.PENDENTE);
        long pedidoFornadaCancelado = resumoPedidoRepository.countByStatusAndPedidoFornadaIdIsNotNull(StatusEnum.CANCELADO);

        Map<String, Long> resultado = new HashMap<>();

        resultado.put("total", pedidoFornada);
        resultado.put("concluídos", pedidoFornadaConcluido);
        resultado.put("pagos", pedidoFornadaPago);
        resultado.put("pendentes", pedidoFornadaPendente);
        resultado.put("cancelados", pedidoFornadaCancelado);

        return resultado;
    }

    @Override
    public List<Map<String, Object>> getBolosMaisPedidos() {
        try {
            List<PedidoBoloEntity> pedidosBolo = pedidoBoloRepository.findAll();
            if (pedidosBolo.isEmpty()) {
                return new ArrayList<>();
            }

            Map<Integer, Long> contagemBolos = pedidosBolo.stream()
                    .filter(p -> p.getBoloId() != null)
                    .collect(Collectors.groupingBy(PedidoBoloEntity::getBoloId, Collectors.counting()));

            return contagemBolos.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                    .map(entry -> {
                        try {
                            Integer boloId = entry.getKey();
                            Long quantidade = entry.getValue();

                            Optional<BoloEntity> boloOpt = boloRepository.findById(boloId);

                            // Filtrar apenas bolos cadastrados (não pedidos de clientes)
                            if (boloOpt.isPresent()) {
                                BoloEntity boloEntity = boloOpt.get();
                                if ("PERSONALIZADO".equals(boloEntity.getCategoria())) {
                                    return null; // Pular bolos que são pedidos de clientes
                                }
                            }

                            Double valorTotal = pedidosBolo.stream()
                                    .filter(p -> p.getBoloId() != null && p.getBoloId().equals(boloId))
                                    .mapToDouble(p -> {
                                        try {
                                            return resumoPedidoRepository
                                                    .findTop1ByPedidoBoloIdAndIsAtivoTrueOrderByDataPedidoDesc(p.getId())
                                                    .map(ResumoPedido::getValor)
                                                    .orElse(0.0);
                                        } catch (Exception e) {
                                            return 0.0;
                                        }
                                    })
                                    .sum();

                            String nomeBolo = "Bolo Desconhecido";
                            if (boloOpt.isPresent()) {
                                BoloEntity boloEntity = boloOpt.get();

                                nomeBolo = boloEntity.getDecoracao() != null ?
                                        decoracaoRepository.findById(boloEntity.getDecoracao())
                                                .map(DecoracaoEntity::getNome)
                                                .orElse("Sem Decoração")
                                        : "Sem Decoração";
                            }

                            Map<String, Object> resultado = new HashMap<>();
                            resultado.put("boloId", boloId);
                            resultado.put("quantidade", quantidade);
                            resultado.put("nome", nomeBolo);
                            resultado.put("valorTotal", valorTotal);
                            return resultado;
                        } catch (Exception e) {
                            System.err.println("Erro ao processar bolo: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Erro em getBolosMaisPedidos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> getFornadasMaisPedidas() {
        try {
            List<PedidoFornada> pedidoFornadas = pedidoFornadaRepository.findAll();
            if (pedidoFornadas.isEmpty()) {
                return new ArrayList<>();
            }

            Map<Integer, Integer> quantidadePorFornadaDaVez = pedidoFornadas.stream()
                    .filter(p -> p.getFornadaDaVez() != null && p.getQuantidade() != null)
                    .collect(Collectors.groupingBy(
                            PedidoFornada::getFornadaDaVez,
                            Collectors.summingInt(PedidoFornada::getQuantidade)
                    ));

            List<FornadaDaVez> fornadasDaVez = fornadaDaVezRepository.findAll();
            if (fornadasDaVez.isEmpty()) {
                return new ArrayList<>();
            }

            Map<Integer, Integer> quantidadePorProduto = new HashMap<>();
            Map<Integer, ProdutoFornada> produtoMap = new HashMap<>();
            Map<Integer, Double> valorTotalPorProduto = new HashMap<>();

            for (FornadaDaVez fdv : fornadasDaVez) {
                try {
                    Integer qtdPedida = quantidadePorFornadaDaVez.getOrDefault(fdv.getId(), 0);
                    Integer produtoId = fdv.getProdutoFornada();

                    if (produtoId == null) continue;

                    quantidadePorProduto.merge(produtoId, qtdPedida, Integer::sum);

                    if (!produtoMap.containsKey(produtoId)) {
                        var produtoEntity = produtoFornadaRepository.findById(produtoId).orElse(null);
                        if (produtoEntity != null) {
                            ProdutoFornada produto = FornadasMapper.toDomain(produtoEntity);
                            produtoMap.put(produtoId, produto);
                            Double valorTotal = qtdPedida * produto.getValor();
                            valorTotalPorProduto.put(produtoId, valorTotal);
                        }
                    } else {
                        ProdutoFornada produto = produtoMap.get(produtoId);
                        if (produto != null) {
                            Double valorAtual = valorTotalPorProduto.getOrDefault(produtoId, 0.0);
                            Double novoValor = valorAtual + (qtdPedida * produto.getValor());
                            valorTotalPorProduto.put(produtoId, novoValor);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar fornada da vez: " + e.getMessage());
                }
            }

            return quantidadePorProduto.entrySet().stream()
                    .map(entry -> {
                        try {
                            Integer produtoId = entry.getKey();
                            ProdutoFornada produto = produtoMap.get(produtoId);

                            Map<String, Object> result = new HashMap<>();
                            result.put("produtoId", produtoId);
                            result.put("nomeProduto", produto != null ? produto.getProduto() : "Produto não encontrado");
                            result.put("quantidadeTotal", entry.getValue());
                            result.put("valorTotal", valorTotalPorProduto.getOrDefault(produtoId, 0.0));
                            return result;
                        } catch (Exception e) {
                            System.err.println("Erro ao criar resultado de fornada: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(map -> produtoMap.get(((Integer) map.get("produtoId"))) != null)
                    .sorted((m1, m2) -> {
                        try {
                            Integer qtd1 = (Integer) m1.get("quantidadeTotal");
                            Integer qtd2 = (Integer) m2.get("quantidadeTotal");
                            if (qtd1 == null || qtd2 == null) return 0;
                            return Integer.compare(qtd2, qtd1);
                        } catch (Exception e) {
                            return 0;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Erro em getFornadasMaisPedidas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Deprecated
    @Override
    public List<Map<String, Object>> getProdutosMaisPedidos() {
        try {
            List<Map<String, Object>> todosProdutos = new ArrayList<>();

            try {
                List<Map<String, Object>> todasFornadas = getFornadasMaisPedidas();
                todasFornadas.forEach(produto -> {
                    Map<String, Object> produtoUnificado = new HashMap<>();
                    produtoUnificado.put("id", produto.get("produtoId"));
                    produtoUnificado.put("nome", produto.get("nomeProduto"));
                    produtoUnificado.put("quantidade", produto.get("quantidadeTotal"));
                    produtoUnificado.put("valorTotal", produto.get("valorTotal"));
                    produtoUnificado.put("tipo", "FORNADA");
                    todosProdutos.add(produtoUnificado);
                });
            } catch (Exception e) {
                System.err.println("Erro ao buscar fornadas: " + e.getMessage());
            }

            try {
                List<Map<String, Object>> todosBolos = getBolosMaisPedidos();
                todosBolos.forEach(bolo -> {
                    Map<String, Object> boloUnificado = new HashMap<>();
                    boloUnificado.put("id", bolo.get("boloId"));
                    boloUnificado.put("nome", bolo.get("nome"));
                    boloUnificado.put("quantidade", bolo.get("quantidade"));
                    boloUnificado.put("valorTotal", bolo.get("valorTotal"));
                    boloUnificado.put("tipo", "BOLO");
                    todosProdutos.add(boloUnificado);
                });
            } catch (Exception e) {
                System.err.println("Erro ao buscar bolos: " + e.getMessage());
            }

            return todosProdutos.stream()
                    .sorted((p1, p2) -> {
                        try {
                            Number qtd1 = (Number) p1.get("quantidade");
                            Number qtd2 = (Number) p2.get("quantidade");
                            if (qtd1 == null || qtd2 == null) return 0;
                            return Long.compare(qtd2.longValue(), qtd1.longValue());
                        } catch (Exception e) {
                            return 0;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Erro geral em getProdutosMaisPedidos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> getProdutosCadastrados() {
        try {
            List<Map<String, Object>> produtosCadastrados = new ArrayList<>();

            // Adicionar bolos cadastrados (excluindo pedidos de clientes com categoria PERSONALIZADO)
            try {
                List<BoloEntity> boloEntities = boloRepository.findAll();
                boloEntities.forEach(bolo -> {
                    // Filtrar bolos que são pedidos de clientes (categoria PERSONALIZADO)
                    if ("PERSONALIZADO".equals(bolo.getCategoria())) {
                        return; // Pular bolos que são pedidos de clientes
                    }

                    // Incluir todos os bolos cadastrados (ativos e inativos)
                    if (bolo.getAtivo() != null) {

                        Map<String, Object> boloMap = new HashMap<>();
                        boloMap.put("id", bolo.getId());

                        // Para bolos, o nome vem da decoração
                        String nomeBolo = "Bolo Personalizado";
                        if (bolo.getDecoracao() != null) {
                            try {
                                var decoracao = decoracaoRepository.findById(bolo.getDecoracao());
                                if (decoracao.isPresent()) {
                                    nomeBolo = decoracao.get().getNome();
                                }
                            } catch (Exception e) {
                                System.err.println("Erro ao buscar decoração do bolo: " + e.getMessage());
                            }
                        }
                        boloMap.put("nome", nomeBolo);
                        boloMap.put("categoria", bolo.getCategoria());

                        // Para bolos, não temos preço fixo - é calculado dinamicamente
                        boloMap.put("preco", 0.0);
                        boloMap.put("tipo", "BOLO");
                        boloMap.put("ativo", bolo.getAtivo());
                        produtosCadastrados.add(boloMap);
                    }
                });
            } catch (Exception e) {
                System.err.println("Erro ao buscar bolos cadastrados: " + e.getMessage());
            }

            // Adicionar produtos de fornada cadastrados
            try {
                List<ProdutoFornada> produtosFornada = produtoFornadaRepository.findAll().stream()
                        .map(FornadasMapper::toDomain)
                        .toList();
                produtosFornada.forEach(produto -> {
                    // Incluir todos os produtos de fornada (ativos e inativos)
                    if (produto.getAtivo() != null) {
                        Map<String, Object> produtoMap = new HashMap<>();
                        produtoMap.put("id", produto.getId());
                        produtoMap.put("nome", produto.getProduto());
                        produtoMap.put("categoria", produto.getCategoria());
                        produtoMap.put("preco", produto.getValor());
                        produtoMap.put("quantidade", 0); // Produtos de fornada não têm quantidade fixa
                        produtoMap.put("tipo", "FORNADA");
                        produtoMap.put("ativo", produto.getAtivo());
                        produtosCadastrados.add(produtoMap);
                    }
                });
            } catch (Exception e) {
                System.err.println("Erro ao buscar produtos de fornada cadastrados: " + e.getMessage());
            }

            return produtosCadastrados;
        } catch (Exception e) {
            System.err.println("Erro geral em getProdutosCadastrados: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> getUltimosPedidos() {
        // Buscar apenas pedidos ativos por data mais recente e limitar para performance
        List<ResumoPedido> resumoPedidos = resumoPedidoRepository
                .findAllByIsAtivoTrueOrderByDataPedidoDesc();

        List<Map<String, Object>> ultimosPedidos = new ArrayList<>();

        for (ResumoPedido resumo : resumoPedidos.stream().limit(50).toList()) {
            Map<String, Object> pedido = new HashMap<>();

            pedido.put("id", resumo.getId());
            pedido.put("valorPedido", resumo.getValor());
            pedido.put("dataPedido", resumo.getDataPedido());
            pedido.put("status", resumo.getStatus());

            boolean pedidoValido = false;

            if (resumo.getPedidoBoloId() != null) {
                PedidoBoloEntity pedidoBoloEntity = pedidoBoloRepository.findById(resumo.getPedidoBoloId()).orElse(null);

                if (pedidoBoloEntity != null && Boolean.TRUE.equals(pedidoBoloEntity.getAtivo())) {
                    pedido.put("nomeDoCliente", pedidoBoloEntity.getNomeCliente());
                    pedido.put("telefoneDoCliente", pedidoBoloEntity.getTelefoneCliente());
                    pedido.put("tipoDoPedido", pedidoBoloEntity.getTipoEntrega());
                    pedido.put("tipoProduto", "BOLO");
                    pedido.put("pedidoBoloId", resumo.getPedidoBoloId());
                    pedidoValido = true;
                }
            } else if (resumo.getPedidoFornadaId() != null) {
                PedidoFornada pedidoFornada = pedidoFornadaRepository.findById(resumo.getPedidoFornadaId()).orElse(null);

                if(pedidoFornada != null && pedidoFornada.isAtivo()) {
                    pedido.put("nomeDoCliente", pedidoFornada.getNomeCliente());
                    pedido.put("telefoneDoCliente", pedidoFornada.getTelefoneCliente());
                    pedido.put("tipoDoPedido", pedidoFornada.getTipoEntrega());
                    pedido.put("tipoProduto", "FORNADA");
                    pedido.put("pedidoFornadaId", resumo.getPedidoFornadaId());
                    pedidoValido = true;
                }
            }
            
            // Só adiciona se o pedido for válido (tem pedidoBolo ou pedidoFornada ativo)
            if (pedidoValido) {
                ultimosPedidos.add(pedido);
            }
        }
        return ultimosPedidos;
    }

    @Override
    public Map<String, Map<String, Long>> countPedidosBolosPorPeriodo(String periodo) {
        List<ResumoPedido> pedidosBolo = resumoPedidoRepository.findByStatusInAndPedidoBoloIdIsNotNull(List.of(StatusEnum.CANCELADO, StatusEnum.CONCLUIDO));
        return processarPedidosPorPeriodoComStatus(pedidosBolo, periodo);
    }

    @Override
    public Map<String, Map<String, Long>> countPedidosFornadaPorPeriodo(String periodo) {
        List<ResumoPedido> pedidosFornada = resumoPedidoRepository.findByStatusInAndPedidoFornadaIdIsNotNull(List.of(StatusEnum.CANCELADO, StatusEnum.CONCLUIDO));
        return processarPedidosPorPeriodoComStatus(pedidosFornada, periodo);
    }

    @Override
    public Map<String, Object> getKPIFornada(Integer fornadaId) {
        try {
            System.out.println("[KPI] getKPIFornada fornadaId=" + fornadaId);
            // Buscar produtos da fornada usando FornadaDaVez
            List<FornadaDaVez> produtosFornada = fornadaDaVezRepository.findByFornada(fornadaId);
            System.out.println("[KPI] FDV itens=" + (produtosFornada != null ? produtosFornada.size() : 0));
            if (produtosFornada != null) {
                for (var fdv : produtosFornada) {
                    System.out.println("[KPI] fdvId=" + fdv.getId() + " produtoFornadaId=" + fdv.getProdutoFornada() + " qtdAtual=" + fdv.getQuantidade());
                }
            }

            if (produtosFornada == null || produtosFornada.isEmpty()) {
                return criarKPIVazio();
            }

            int quantidadeDisponivel = 0;
            int quantidadeVendida = 0;
            double totalDisponivel = 0.0;
            double totalVendido = 0.0;

            for (FornadaDaVez fornadaDaVez : produtosFornada) {
                // Buscar o produto da fornada
                var produtoEntity = produtoFornadaRepository.findById(fornadaDaVez.getProdutoFornada())
                        .orElse(null);
                ProdutoFornada produto = produtoEntity != null ? FornadasMapper.toDomain(produtoEntity) : null;
                if (produto == null) continue;

                // Buscar pedidos para este produto da fornada
                List<PedidoFornada> pedidos = pedidoFornadaRepository.findAll().stream()
                        .filter(p -> p.getFornadaDaVez() != null && p.getFornadaDaVez().equals(fornadaDaVez.getId()))
                        .toList();

                // Quantidade vendida (soma dos pedidos)
                int qtdVendida = pedidos.stream()
                        .mapToInt(PedidoFornada::getQuantidade)
                        .sum();
                quantidadeVendida += qtdVendida;

                // Quantidade planejada original = quantidade restante atual + quantidade já vendida
                int qtdAtual = fornadaDaVez.getQuantidade() != null ? fornadaDaVez.getQuantidade() : 0;
                int qtdPlanejada = qtdAtual + qtdVendida;
                quantidadeDisponivel += qtdPlanejada;

                // Totais monetários
                double valorDisponivel = qtdPlanejada * produto.getValor();
                totalDisponivel += valorDisponivel;

                double valorVendido = qtdVendida * produto.getValor();
                totalVendido += valorVendido;
            }

            // Calcular valor perdido
            double valorPerdido = totalDisponivel - totalVendido;

            Map<String, Object> kpi = new HashMap<>();
            kpi.put("quantidadeDisponivel", quantidadeDisponivel);
            kpi.put("quantidadeVendida", quantidadeVendida);
            kpi.put("totalDisponivel", totalDisponivel);
            kpi.put("totalVendido", totalVendido);
            kpi.put("valorPerdido", valorPerdido);
            kpi.put("percentualVendido", quantidadeDisponivel > 0 ?
                    (double) quantidadeVendida / quantidadeDisponivel * 100 : 0.0);

            return kpi;
        } catch (Exception e) {
            System.err.println("Erro em getKPIFornada: " + e.getMessage());
            return criarKPIVazio();
        }
    }

    @Override
    public Map<String, Object> getKPIFornadaMaisRecente() {
        try {
            LocalDate hoje = LocalDate.now();
            System.out.println("[KPI] getKPIFornadaMaisRecente hoje=" + hoje);
            var todas = fornadaRepository.findAll();
            System.out.println("[KPI] total fornadas=" + todas.size());
            Optional<Fornada> ultimaEncerrada = todas
                    .stream()
                    // Última ENCERRADA: inativa e já iniciada
                    .filter(f -> !Boolean.TRUE.equals(f.isAtivo()))
                    .filter(f -> f.getDataInicio() != null && !f.getDataInicio().isAfter(hoje))


                    .peek(f -> System.out.println("[KPI] candidata id=" + f.getId() + " ini=" + f.getDataInicio() + " fim=" + f.getDataFim()))
                    // Critério de "última": maior ID (encerrou por último em nossa modelagem)
                    .max(Comparator.comparingInt(Fornada::getId));

            if (ultimaEncerrada.isEmpty()) {
                System.out.println("[KPI] nenhuma encerrada encontrada");
                return criarKPIVazio();
            }
            System.out.println("[KPI] selecionada id=" + ultimaEncerrada.get().getId());
            Map<String, Object> kpi = getKPIFornada(ultimaEncerrada.get().getId());
            // incluir intervalo para exibição
            kpi.put("dataInicio", ultimaEncerrada.get().getDataInicio());
            kpi.put("dataFim", ultimaEncerrada.get().getDataFim());
            kpi.put("fornadaId", ultimaEncerrada.get().getId());
            return kpi;
        } catch (Exception e) {
            System.err.println("Erro em getKPIFornadaMaisRecente: " + e.getMessage());
            return criarKPIVazio();
        }
    }

    @Override
    public Map<String, Object> getKPIFornadasPorMesAno(int ano, int mes) {
        try {
            List<Fornada> fornadas = fornadaRepository.findByDataInicioBetweenOrderByDataInicioAsc(
                    LocalDate.of(ano, mes, 1),
                    LocalDate.of(ano, mes, 1).plusMonths(1).minusDays(1)
            );

            if (fornadas.isEmpty()) {
                return criarKPIVazio();
            }

            double totalDisponivel = 0.0;            double totalVendido = 0.0;
            int quantidadeDisponivel = 0;
            int quantidadeVendida = 0;

            for (Fornada fornada : fornadas) {
                Map<String, Object> kpiFornada = getKPIFornada(fornada.getId());

                quantidadeDisponivel += (Integer) kpiFornada.get("quantidadeDisponivel");
                quantidadeVendida += (Integer) kpiFornada.get("quantidadeVendida");
                totalDisponivel += (Double) kpiFornada.get("totalDisponivel");
                totalVendido += (Double) kpiFornada.get("totalVendido");
            }

            double valorPerdido = totalDisponivel - totalVendido;

            Map<String, Object> kpi = new HashMap<>();
            kpi.put("quantidadeDisponivel", quantidadeDisponivel);
            kpi.put("quantidadeVendida", quantidadeVendida);
            kpi.put("totalDisponivel", totalDisponivel);
            kpi.put("totalVendido", totalVendido);
            kpi.put("valorPerdido", valorPerdido);
            kpi.put("percentualVendido", quantidadeDisponivel > 0 ?
                    (double) quantidadeVendida / quantidadeDisponivel * 100 : 0.0);
            kpi.put("mes", mes);
            kpi.put("ano", ano);

            return kpi;
        } catch (Exception e) {
            System.err.println("Erro em getKPIFornadasPorMesAno: " + e.getMessage());
            return criarKPIVazio();
        }
    }

    private Map<String, Object> criarKPIVazio() {
        Map<String, Object> kpi = new HashMap<>();
        kpi.put("quantidadeDisponivel", 0);
        kpi.put("quantidadeVendida", 0);
        kpi.put("totalDisponivel", 0.0);
        kpi.put("totalVendido", 0.0);
        kpi.put("valorPerdido", 0.0);
        kpi.put("percentualVendido", 0.0);
        return kpi;
    }

    private Map<String, Map<String, Long>> processarPedidosPorPeriodoComStatus(List<ResumoPedido> pedidos, String periodo) {
        Map<String, Map<String, Long>> resultado = new HashMap<>();
        if (periodo.equalsIgnoreCase("mes")) {
            Map<String, List<ResumoPedido>> pedidosPorMes = pedidos.stream()
                    .collect(Collectors.groupingBy(
                            pedido -> {
                                int mes = pedido.getDataPedido().getMonthValue();
                                return String.format("%02d", mes);
                            }
                    ));
            for (int i = 1; i <= 12 ; i++) {
                String mesKey = String.format("%02d", i);
                List<ResumoPedido> pedidosDoMes = pedidosPorMes.getOrDefault(mesKey, new ArrayList<>());

                Map<String, Long> statusCount = new HashMap<>();
                statusCount.put("cancelados",
                        pedidosDoMes.stream()
                                .filter(p -> p.getStatus() == StatusEnum.CANCELADO)
                                .count());
                statusCount.put("concluidos",
                        pedidosDoMes.stream()
                                .filter(p -> p.getStatus() == StatusEnum.CONCLUIDO)
                                .count());
                resultado.put(mesKey, statusCount);
            }
        } else if(periodo.equalsIgnoreCase("ano")) {
            Map<String, List<ResumoPedido>> pedidosPorAno = pedidos.stream()
                    .collect(Collectors.groupingBy(
                            pedido -> String.valueOf(pedido.getDataPedido().getYear())
                    ));
            if (pedidosPorAno.isEmpty()) {
                String anoAtual = String.valueOf(LocalDateTime.now().getYear());
                Map<String, Long> statusCount = new HashMap<>();
                statusCount.put("cancelados", 0L);
                statusCount.put("concluidos", 0L);
                resultado.put(anoAtual, statusCount);
            } else {
                pedidosPorAno.forEach((ano, pedidosDoAno) -> {
                    Map<String, Long> statusCount = new HashMap<>();
                    statusCount.put("cancelados",
                            pedidosDoAno.stream()
                                    .filter(p -> p.getStatus() == StatusEnum.CANCELADO)
                                    .count());
                    statusCount.put("concluidos",
                            pedidosDoAno.stream()
                                    .filter(p -> p.getStatus() == StatusEnum.CONCLUIDO)
                                    .count());
                    resultado.put(ano, statusCount);
                });
            }
        }
        return resultado;
    }

    @Override
    public List<Map<String, Object>> getMassasPendentes() {
        try {
            // Buscar todos os resumos de pedidos pendentes e pagos que são de bolo
            // (pedidos que ainda não foram entregues e precisam ser produzidos)
            List<ResumoPedido> pedidosPendentes = resumoPedidoRepository
                    .findByStatusInAndPedidoBoloIdIsNotNull(
                            List.of(StatusEnum.PENDENTE, StatusEnum.PAGO)
                    );

            if (pedidosPendentes.isEmpty()) {
                return new ArrayList<>();
            }

            // Agrupar por massaId e contar
            Map<Integer, Long> contagemPorMassa = new HashMap<>();
            Map<Integer, String> nomesMassas = new HashMap<>();

            for (ResumoPedido resumo : pedidosPendentes) {
                try {
                    PedidoBoloEntity pedidoBolo = pedidoBoloRepository
                            .findById(resumo.getPedidoBoloId())
                            .orElse(null);

                    if (pedidoBolo == null || !Boolean.TRUE.equals(pedidoBolo.getAtivo())) {
                        continue;
                    }

                    BoloEntity bolo = boloRepository
                            .findById(pedidoBolo.getBoloId())
                            .orElse(null);

                    if (bolo == null || bolo.getMassa() == null) {
                        continue;
                    }

                    Integer massaId = bolo.getMassa();
                    contagemPorMassa.put(massaId, contagemPorMassa.getOrDefault(massaId, 0L) + 1);

                    // Buscar nome da massa se ainda não foi buscado
                    if (!nomesMassas.containsKey(massaId)) {
                        massaRepository.findById(massaId)
                                .ifPresent(massa -> nomesMassas.put(massaId, massa.getSabor()));
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar pedido: " + e.getMessage());
                }
            }

            // Converter para lista de Map
            return contagemPorMassa.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> resultado = new HashMap<>();
                        resultado.put("massaId", entry.getKey());
                        resultado.put("nomeMassa", nomesMassas.getOrDefault(entry.getKey(), "Massa Desconhecida"));
                        resultado.put("quantidade", entry.getValue());
                        return resultado;
                    })
                    .sorted((m1, m2) -> Long.compare(
                            (Long) m2.get("quantidade"),
                            (Long) m1.get("quantidade")
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Erro em getMassasPendentes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> getRecheiosPendentes() {
        try {
            // Buscar todos os resumos de pedidos pendentes e pagos que são de bolo
            // (pedidos que ainda não foram entregues e precisam ser produzidos)
            List<ResumoPedido> pedidosPendentes = resumoPedidoRepository
                    .findByStatusInAndPedidoBoloIdIsNotNull(
                            List.of(StatusEnum.PENDENTE, StatusEnum.PAGO)
                    );

            if (pedidosPendentes.isEmpty()) {
                return new ArrayList<>();
            }

            // Agrupar por recheio e contar
            Map<String, Long> contagemPorRecheio = new HashMap<>();
            Map<String, Integer> recheioIds = new HashMap<>();

            for (ResumoPedido resumo : pedidosPendentes) {
                try {
                    PedidoBoloEntity pedidoBolo = pedidoBoloRepository
                            .findById(resumo.getPedidoBoloId())
                            .orElse(null);

                    if (pedidoBolo == null || !Boolean.TRUE.equals(pedidoBolo.getAtivo())) {
                        continue;
                    }

                    BoloEntity bolo = boloRepository
                            .findById(pedidoBolo.getBoloId())
                            .orElse(null);

                    if (bolo == null || bolo.getRecheioPedido() == null) {
                        continue;
                    }

                    Integer recheioPedidoId = bolo.getRecheioPedido();
                    String nomeRecheio = obterNomeRecheio(recheioPedidoId);

                    if (nomeRecheio != null && !nomeRecheio.isEmpty()) {
                        contagemPorRecheio.put(nomeRecheio,
                                contagemPorRecheio.getOrDefault(nomeRecheio, 0L) + 1);
                        recheioIds.put(nomeRecheio, recheioPedidoId);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar pedido: " + e.getMessage());
                }
            }

            // Converter para lista de Map
            return contagemPorRecheio.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> resultado = new HashMap<>();
                        resultado.put("recheioId", recheioIds.get(entry.getKey()));
                        resultado.put("nomeRecheio", entry.getKey());
                        resultado.put("quantidade", entry.getValue());
                        return resultado;
                    })
                    .sorted((m1, m2) -> Long.compare(
                            (Long) m2.get("quantidade"),
                            (Long) m1.get("quantidade")
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Erro em getRecheiosPendentes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private String obterNomeRecheio(Integer recheioPedidoId) {
        try {
            RecheioPedidoEntity recheioPedido = recheioPedidoRepository
                    .findById(recheioPedidoId)
                    .orElse(null);

            if (recheioPedido == null) {
                return null;
            }

            // Caso 1: Recheio Exclusivo
            if (recheioPedido.getRecheioExclusivo() != null) {
                RecheioExclusivoEntity recheioExclusivo = recheioExclusivoRepository
                        .findById(recheioPedido.getRecheioExclusivo())
                        .orElse(null);

                if (recheioExclusivo != null && recheioExclusivo.getNome() != null) {
                    return recheioExclusivo.getNome();
                }
            }

            // Caso 2: Dois Recheios Unitários
            List<String> sabores = new ArrayList<>();

            if (recheioPedido.getRecheioUnitarioId1() != null) {
                recheioUnitarioRepository.findById(recheioPedido.getRecheioUnitarioId1())
                        .ifPresent(recheio -> {
                            if (recheio.getSabor() != null) {
                                sabores.add(recheio.getSabor());
                            }
                        });
            }

            if (recheioPedido.getRecheioUnitarioId2() != null) {
                recheioUnitarioRepository.findById(recheioPedido.getRecheioUnitarioId2())
                        .ifPresent(recheio -> {
                            if (recheio.getSabor() != null) {
                                sabores.add(recheio.getSabor());
                            }
                        });
            }

            if (sabores.isEmpty()) {
                return null;
            }

            // Formatar nome: "Sabor1 com Sabor2" ou apenas "Sabor1" se houver apenas um
            return sabores.size() == 1
                    ? sabores.get(0)
                    : String.join(" com ", sabores);

        } catch (Exception e) {
            System.err.println("Erro ao obter nome do recheio: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getPedidosProximosDaEntrega(int diasProximos) {
        try {
            LocalDate hoje = LocalDate.now();
            LocalDate dataFim = hoje.plusDays(diasProximos);
            LocalDateTime hojeDateTime = hoje.atStartOfDay();
            LocalDateTime dataFimDateTime = dataFim.atTime(23, 59, 59);

            // Buscar todos os pedidos ativos que ainda não foram entregues
            List<ResumoPedido> resumosPedidos = resumoPedidoRepository
                    .findByStatusInAndIsAtivoTrue(List.of(StatusEnum.PENDENTE, StatusEnum.PAGO));

            if (resumosPedidos.isEmpty()) {
                return new ArrayList<>();
            }

            List<Map<String, Object>> resultado = new ArrayList<>();

            for (ResumoPedido resumo : resumosPedidos) {
                try {
                    LocalDate dataEntregaPedido = null;
                    Map<String, Object> pedido = new HashMap<>();
                    pedido.put("resumoPedidoId", resumo.getId());
                    pedido.put("valor", resumo.getValor());
                    pedido.put("status", resumo.getStatus());

                    // Buscar detalhes do pedido e obter data de entrega
                    if (resumo.getPedidoBoloId() != null) {
                        PedidoBoloEntity pedidoBolo = pedidoBoloRepository
                                .findById(resumo.getPedidoBoloId())
                                .orElse(null);

                        if (pedidoBolo != null && Boolean.TRUE.equals(pedidoBolo.getAtivo())) {
                            // Usar dataPrevisaoEntrega do pedido como data de entrega
                            dataEntregaPedido = pedidoBolo.getDataPrevisaoEntrega();
                            
                            pedido.put("nomeCliente", pedidoBolo.getNomeCliente());
                            pedido.put("telefoneCliente", pedidoBolo.getTelefoneCliente());
                            pedido.put("tipoEntrega", pedidoBolo.getTipoEntrega());
                            pedido.put("dataPrevisaoEntrega", pedidoBolo.getDataPrevisaoEntrega());
                            pedido.put("tipoProduto", "BOLO");
                            pedido.put("pedidoBoloId", resumo.getPedidoBoloId());
                        } else {
                            continue; // Pular se pedido não está ativo
                        }
                    } else if (resumo.getPedidoFornadaId() != null) {
                        PedidoFornada pedidoFornada = pedidoFornadaRepository
                                .findById(resumo.getPedidoFornadaId())
                                .orElse(null);

                        if (pedidoFornada != null && Boolean.TRUE.equals(pedidoFornada.getAtivo())) {
                            // Usar dataPrevisaoEntrega do pedido como data de entrega
                            dataEntregaPedido = pedidoFornada.getDataPrevisaoEntrega();
                            
                            pedido.put("nomeCliente", pedidoFornada.getNomeCliente());
                            pedido.put("telefoneCliente", pedidoFornada.getTelefoneCliente());
                            pedido.put("tipoEntrega", pedidoFornada.getTipoEntrega());
                            pedido.put("dataPrevisaoEntrega", pedidoFornada.getDataPrevisaoEntrega());
                            pedido.put("tipoProduto", "FORNADA");
                            pedido.put("pedidoFornadaId", resumo.getPedidoFornadaId());
                        } else {
                            continue; // Pular se pedido não está ativo
                        }
                    } else {
                        continue; // Pular se não tem pedido associado
                    }

                    // Filtrar pedidos por data de entrega (priorizar pedidos futuros de 2025, excluir antigos de 2024)
                    if (dataEntregaPedido != null) {
                        int anoDataEntrega = dataEntregaPedido.getYear();
                        int anoAtual = hoje.getYear();
                        
                        // Verificar também o ano do pedido para garantir que são pedidos recentes
                        int anoPedido = resumo.getDataPedido() != null ? resumo.getDataPedido().getYear() : anoAtual;
                        
                        // EXCLUIR pedidos de anos muito antigos (2023 ou anteriores)
                        if (anoDataEntrega < anoAtual - 1 || anoPedido < anoAtual - 1) {
                            continue; // Excluir pedidos de 2023 ou anteriores
                        }
                        
                        // Se estamos em 2025 ou posterior, excluir explicitamente pedidos de 2024
                        // Isso garante que apenas pedidos de 2025 ou futuros sejam exibidos
                        if (anoAtual >= 2025) {
                            if (anoDataEntrega < 2025) {
                                continue; // Excluir pedidos de 2024 quando estamos em 2025 ou posterior
                            }
                        }
                        
                        // Focar em pedidos futuros ou muito recentes
                        LocalDate limiteAtrasado = hoje.minusDays(2); // Apenas últimos 2 dias para atrasados
                        LocalDate limiteFuturo = hoje.plusDays(90); // Até 90 dias no futuro
                        
                        // PRIORIDADE 1: Pedidos nos próximos N dias (entre hoje e hoje + N dias)
                        boolean estaNoIntervaloProximo = !dataEntregaPedido.isBefore(hoje) && !dataEntregaPedido.isAfter(dataFim);
                        
                        // PRIORIDADE 2: Pedidos futuros além do intervalo mas dentro de 90 dias
                        boolean estaFuturoAlemIntervalo = dataEntregaPedido.isAfter(dataFim) && !dataEntregaPedido.isAfter(limiteFuturo);
                        
                        // PRIORIDADE 3: Pedidos atrasados muito recentes (apenas últimos 2 dias) do ano atual
                        // Isso evita incluir pedidos de 2024 que estão muito atrasados
                        boolean estaAtrasadoMuitoRecente = dataEntregaPedido.isBefore(hoje) 
                                && !dataEntregaPedido.isBefore(limiteAtrasado)
                                && anoDataEntrega == anoAtual; // Apenas do ano atual
                        
                        // Incluir apenas pedidos futuros relevantes ou atrasados muito recentemente
                        // Priorizar pedidos futuros de 2025 e excluir pedidos antigos de 2024
                        if (estaNoIntervaloProximo || estaFuturoAlemIntervalo || estaAtrasadoMuitoRecente) {
                            // Converter para LocalDateTime para manter consistência
                            LocalDateTime dataEntregaDateTime = dataEntregaPedido.atStartOfDay();
                            pedido.put("dataEntrega", dataEntregaDateTime);
                            resultado.add(pedido);
                        }
                    }
                    // Se dataPrevisaoEntrega for null, não incluir (evitar pedidos sem data clara)
                } catch (Exception e) {
                    System.err.println("Erro ao processar pedido próximo: " + e.getMessage());
                }
            }

            // Ordenar por data de entrega
            resultado.sort((p1, p2) -> {
                try {
                    LocalDateTime data1 = (LocalDateTime) p1.get("dataEntrega");
                    LocalDateTime data2 = (LocalDateTime) p2.get("dataEntrega");
                    if (data1 == null && data2 == null) return 0;
                    if (data1 == null) return 1;
                    if (data2 == null) return -1;
                    return data1.compareTo(data2);
                } catch (Exception e) {
                    return 0;
                }
            });

            return resultado;

        } catch (Exception e) {
            System.err.println("Erro em getPedidosProximosDaEntrega: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> getItensMaisPedidosPorPeriodo(String tipoItem, String periodo, Integer ano, Integer mes) {
        try {
            // Buscar todos os resumos de pedidos de bolo ativos
            List<ResumoPedido> todosResumosPedidos = resumoPedidoRepository
                    .findByPedidoBoloIdIsNotNullAndIsAtivoTrue();

            if (todosResumosPedidos.isEmpty()) {
                return new ArrayList<>();
            }

            // Filtrar por período se fornecido
            LocalDateTime dataInicio = null;
            LocalDateTime dataFim = null;
            LocalDateTime agora = LocalDateTime.now();

            if (ano != null) {
                if (mes != null) {
                    // Filtrar por mês específico
                    dataInicio = LocalDateTime.of(ano, mes, 1, 0, 0);
                    dataFim = LocalDateTime.of(ano, mes, dataInicio.toLocalDate().lengthOfMonth(), 23, 59, 59);
                } else {
                    // Filtrar por ano completo - ajustar para incluir até o último dia do ano
                    dataInicio = LocalDateTime.of(ano, 1, 1, 0, 0, 0);
                    dataFim = LocalDateTime.of(ano, 12, 31, 23, 59, 59);
                }
            } else if (periodo != null) {
                switch (periodo.toUpperCase()) {
                    case "SEMANA":
                        dataInicio = agora.minusWeeks(1);
                        dataFim = agora;
                        break;
                    case "MES":
                        dataInicio = agora.minusMonths(1);
                        dataFim = agora;
                        break;
                    case "ANO":
                        dataInicio = agora.minusYears(1);
                        dataFim = agora;
                        break;
                }
            }

            // Filtrar por período e excluir apenas CANCELADOS
            final LocalDateTime inicio = dataInicio;
            final LocalDateTime fim = dataFim;
            List<ResumoPedido> resumosPedidos = todosResumosPedidos.stream()
                    .filter(rp -> {
                        // Excluir apenas pedidos cancelados
                        if (rp.getStatus() == StatusEnum.CANCELADO) {
                            return false;
                        }
                        // Filtrar por período se fornecido
                        if (inicio != null && fim != null) {
                            return rp.getDataPedido() != null &&
                                    !rp.getDataPedido().isBefore(inicio) &&
                                    !rp.getDataPedido().isAfter(fim);
                        }
                        return true;
                    })
                    .collect(Collectors.toList());

            if (resumosPedidos.isEmpty()) {
                return new ArrayList<>();
            }

            Map<String, Long> contagemPorItem = new HashMap<>();
            Map<String, Integer> itemIds = new HashMap<>();

            for (ResumoPedido resumo : resumosPedidos) {
                try {
                    // Pular pedidos cancelados
                    if (resumo.getStatus() == StatusEnum.CANCELADO) {
                        continue;
                    }
                    
                    PedidoBoloEntity pedidoBolo = pedidoBoloRepository
                            .findById(resumo.getPedidoBoloId())
                            .orElse(null);

                    if (pedidoBolo == null || !Boolean.TRUE.equals(pedidoBolo.getAtivo())) {
                        continue;
                    }

                    BoloEntity bolo = boloRepository
                            .findById(pedidoBolo.getBoloId())
                            .orElse(null);

                    if (bolo == null) {
                        continue;
                    }

                    AtomicReference<String> nomeItem = new AtomicReference<>();
                    Integer itemId = null;
                    String periodoKey = null;

                    switch (tipoItem.toUpperCase()) {
                        case "MASSA":
                            if (bolo.getMassa() != null) {
                                itemId = bolo.getMassa();
                                massaRepository.findById(itemId)
                                        .ifPresent(massa -> nomeItem.set(massa.getSabor()));
                            }
                            break;
                        case "RECHEIO":
                            if (bolo.getRecheioPedido() != null) {
                                itemId = bolo.getRecheioPedido();
                                nomeItem.set(obterNomeRecheio(itemId));
                            }
                            break;
                        case "DECORACAO":
                            if (bolo.getDecoracao() != null) {
                                itemId = bolo.getDecoracao();
                                decoracaoRepository.findById(itemId)
                                        .ifPresent(decoracao -> nomeItem.set(decoracao.getNome()));
                            }
                            break;
                    }

                    if (nomeItem.get() != null && !nomeItem.get().isEmpty()) {
                        // Criar chave de período
                        if (periodo != null && periodo.equalsIgnoreCase("MES") && resumo.getDataPedido() != null) {
                            // Quando período=MES e ano é fornecido, agrupar por mês dentro do ano
                            int anoPedido = resumo.getDataPedido().getYear();
                            int mesPedido = resumo.getDataPedido().getMonthValue();
                            
                            // Se ano foi fornecido, validar se o pedido pertence ao ano e incluir todos os meses (1-12)
                            if (ano != null && anoPedido == ano && mesPedido >= 1 && mesPedido <= 12) {
                                periodoKey = String.format("%04d-%02d", anoPedido, mesPedido);
                            } else if (ano == null) {
                                // Se ano não foi fornecido, usar o ano do pedido (incluir todos os meses)
                                if (mesPedido >= 1 && mesPedido <= 12) {
                                    periodoKey = String.format("%04d-%02d", anoPedido, mesPedido);
                                }
                            } else {
                                continue; // Pular pedidos de outros anos
                            }
                        } else if (periodo != null && periodo.equalsIgnoreCase("ANO") && resumo.getDataPedido() != null) {
                            periodoKey = String.valueOf(resumo.getDataPedido().getYear());
                        } else if (periodo != null && periodo.equalsIgnoreCase("SEMANA") && resumo.getDataPedido() != null) {
                            // Calcular semana do ano
                            int semana = resumo.getDataPedido().getDayOfYear() / 7 + 1;
                            periodoKey = String.format("%04d-S%02d", resumo.getDataPedido().getYear(), semana);
                        } else {
                            periodoKey = "TODOS";
                        }

                        if (periodoKey != null) {
                            String chave = nomeItem + "|" + periodoKey;
                            contagemPorItem.put(chave, contagemPorItem.getOrDefault(chave, 0L) + 1);
                            itemIds.put(chave, itemId);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar pedido: " + e.getMessage());
                }
            }

            // Converter para lista de Map
            return contagemPorItem.entrySet().stream()
                    .map(entry -> {
                        String[] parts = entry.getKey().split("\\|");
                        Map<String, Object> resultado = new HashMap<>();
                        resultado.put("itemId", itemIds.get(entry.getKey()));
                        resultado.put("nomeItem", parts[0]);
                        resultado.put("periodo", parts.length > 1 ? parts[1] : "TODOS");
                        resultado.put("quantidade", entry.getValue());
                        return resultado;
                    })
                    .sorted((m1, m2) -> Long.compare(
                            (Long) m2.get("quantidade"),
                            (Long) m1.get("quantidade")
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Erro em getItensMaisPedidosPorPeriodo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
