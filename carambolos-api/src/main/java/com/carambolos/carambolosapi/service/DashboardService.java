package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.model.*;
import com.carambolos.carambolosapi.model.enums.StatusEnum;
import com.carambolos.carambolosapi.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final PedidoBoloRepository pedidoBoloRepository;
    private final UsuarioRepository usuarioRepository;
    private final BoloRepository boloRepository;
    private final PedidoFornadaRepository pedidoFornadaRepository;
    private final MassaRepository massaRepository;
    private final RecheioPedidoRepository recheioPedidoRepository;
    private final RecheioUnitarioRepository recheioUnitarioRepository;
    private final ProdutoFornadaRepository produtoFornadaRepository;
    private final FornadaDaVezRepository fornadaDaVezRepository;
    private final ResumoPedidoRepository resumoPedidoRepository;
    private final DecoracaoRepository decoracaoRepository;
    private final FornadaRepository fornadaRepository;

    public DashboardService(PedidoBoloRepository pedidoBoloRepository,
                            UsuarioRepository usuarioRepository,
                            BoloRepository boloRepository,
                            PedidoFornadaRepository pedidoFornadaRepository,
                            MassaRepository massaRepository,
                            RecheioPedidoRepository recheioPedidoRepository,
                            RecheioUnitarioRepository recheioUnitarioRepository,
                            ProdutoFornadaRepository produtoFornadaRepository,
                            FornadaDaVezRepository fornadaDaVezRepository, ResumoPedidoRepository resumoPedidoRepository, DecoracaoRepository decoracaoRepository, FornadaRepository fornadaRepository) {
        this.pedidoBoloRepository = pedidoBoloRepository;
        this.usuarioRepository = usuarioRepository;
        this.boloRepository = boloRepository;
        this.pedidoFornadaRepository = pedidoFornadaRepository;
        this.massaRepository = massaRepository;
        this.recheioPedidoRepository = recheioPedidoRepository;
        this.recheioUnitarioRepository = recheioUnitarioRepository;
        this.produtoFornadaRepository = produtoFornadaRepository;
        this.fornadaDaVezRepository = fornadaDaVezRepository;
        this.resumoPedidoRepository = resumoPedidoRepository;
        this.decoracaoRepository = decoracaoRepository;
        this.fornadaRepository = fornadaRepository;
    }

    public long qtdClientesUnicos() {
        List<PedidoBolo> pedidosBolo = pedidoBoloRepository.findAll();
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

    public List<Map<String, Object>> getBolosMaisPedidos() {
        try {
            List<PedidoBolo> pedidosBolo = pedidoBoloRepository.findAll();
            if (pedidosBolo.isEmpty()) {
                return new ArrayList<>();
            }

            Map<Integer, Long> contagemBolos = pedidosBolo.stream()
                    .filter(p -> p.getBoloId() != null)
                    .collect(Collectors.groupingBy(PedidoBolo::getBoloId, Collectors.counting()));

            return contagemBolos.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                    .map(entry -> {
                        try {
                            Integer boloId = entry.getKey();
                            Long quantidade = entry.getValue();

                            Optional<Bolo> boloOpt = boloRepository.findById(boloId);

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
                                Bolo bolo = boloOpt.get();

                                String decoracaoNome = bolo.getDecoracao() != null ?
                                        decoracaoRepository.findById(bolo.getDecoracao())
                                                .map(Decoracao::getNome)
                                                .orElse("Sem Decoração")
                                        : "Sem Decoração";

                                nomeBolo = decoracaoNome;
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
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

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
                        ProdutoFornada produto = produtoFornadaRepository.findById(produtoId).orElse(null);
                        if (produto != null) {
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
                    continue;
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
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Map<String, Object>> getProdutosMaisPedidos() {
        try {
            List<Map<String, Object>> todosProdutos = new ArrayList<>();

            // Adicionar produtos de fornada
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
                e.printStackTrace();
            }

            // Adicionar bolos
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
                e.printStackTrace();
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
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Map<String, Object>> getUltimosPedidos() {
        // Buscar por data mais recente (independente do status) e limitar para performance
        List<ResumoPedido> resumoPedidos = resumoPedidoRepository
                .findAllByOrderByDataPedidoDesc();

        List<Map<String, Object>> ultimosPedidos = new ArrayList<>();

        for (ResumoPedido resumo : resumoPedidos.stream().limit(50).collect(Collectors.toList())) {
            Map<String, Object> pedido = new HashMap<>();

            pedido.put("id", resumo.getId());
            pedido.put("valorPedido", resumo.getValor());
            pedido.put("dataPedido", resumo.getDataPedido());
            pedido.put("status", resumo.getStatus());

            if (resumo.getPedidoBoloId() != null) {
                PedidoBolo pedidoBolo = pedidoBoloRepository.findById(resumo.getPedidoBoloId()).orElse(null);

                if (pedidoBolo != null) {
                    pedido.put("nomeDoCliente", pedidoBolo.getNomeCliente());
                    pedido.put("telefoneDoCliente", pedidoBolo.getTelefoneCliente());
                    pedido.put("tipoDoPedido", pedidoBolo.getTipoEntrega());
                    pedido.put("tipoProduto", "BOLO");
                    pedido.put("pedidoBoloId", resumo.getPedidoBoloId());
                }
            } else if (resumo.getPedidoFornadaId() != null) {
                PedidoFornada pedidoFornada = pedidoFornadaRepository.findById(resumo.getPedidoFornadaId()).orElse(null);

                if(pedidoFornada != null) {
                    pedido.put("nomeDoCliente", pedidoFornada.getNomeCliente());
                    pedido.put("telefoneDoCliente", pedidoFornada.getTelefoneCliente());
                    pedido.put("tipoDoPedido", pedidoFornada.getTipoEntrega());
                    pedido.put("tipoProduto", "FORNADA");
                    pedido.put("pedidoFornadaId", resumo.getPedidoFornadaId());
                }
            }
            ultimosPedidos.add(pedido);
        }
        return ultimosPedidos;
    }

    public Map<String, Map<String, Long>> processarPedidosPorPeriodoComStatus(List<ResumoPedido> pedidos, String periodo) {
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

    public Map<String, Map<String, Long>> countPedidosBolosPorPeriodo(String periodo) {
        List<ResumoPedido> pedidosBolo = resumoPedidoRepository.findByStatusInAndPedidoBoloIdIsNotNull(List.of(StatusEnum.CANCELADO, StatusEnum.CONCLUIDO));
        return processarPedidosPorPeriodoComStatus(pedidosBolo, periodo);
    }

    public Map<String, Map<String, Long>> countPedidosFornadaPorPeriodo(String periodo) {
        List<ResumoPedido> pedidosFornada = resumoPedidoRepository.findByStatusInAndPedidoFornadaIdIsNotNull(List.of(StatusEnum.CANCELADO, StatusEnum.CONCLUIDO));
        return processarPedidosPorPeriodoComStatus(pedidosFornada, periodo);
    }

    // KPIs específicos para fornadas
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
            
            if (produtosFornada.isEmpty()) {
                return criarKPIVazio();
            }

            int quantidadeDisponivel = 0;
            int quantidadeVendida = 0;
            double totalDisponivel = 0.0;
            double totalVendido = 0.0;

            for (FornadaDaVez fornadaDaVez : produtosFornada) {
                // Buscar o produto da fornada
                ProdutoFornada produto = produtoFornadaRepository.findById(fornadaDaVez.getProdutoFornada())
                    .orElse(null);
                if (produto == null) continue;

                // Buscar pedidos para este produto da fornada
                List<PedidoFornada> pedidos = pedidoFornadaRepository.findAll().stream()
                    .filter(p -> p.getFornadaDaVez() != null && p.getFornadaDaVez().equals(fornadaDaVez.getId()))
                    .collect(Collectors.toList());

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
            e.printStackTrace();
            return criarKPIVazio();
        }
    }

    public Map<String, Object> getKPIFornadaMaisRecente() {
        try {
            LocalDate hoje = LocalDate.now();
            System.out.println("[KPI] getKPIFornadaMaisRecente hoje=" + hoje);
            var todas = fornadaRepository.findAll();
            System.out.println("[KPI] total fornadas=" + (todas != null ? todas.size() : 0));
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
            e.printStackTrace();
            return criarKPIVazio();
        }
    }

    public Map<String, Object> getKPIFornadasPorMesAno(int ano, int mes) {
        try {
            List<Fornada> fornadas = fornadaRepository.findByIsAtivoTrueAndDataInicioBetweenOrderByDataInicioAsc(
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
            e.printStackTrace();
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

}
