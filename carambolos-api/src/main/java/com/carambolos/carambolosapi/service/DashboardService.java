package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.model.*;
import com.carambolos.carambolosapi.model.enums.StatusEnum;
import com.carambolos.carambolosapi.repository.*;
import org.springframework.stereotype.Service;
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

    public long countPedidosByStatusConcluido() {
        return resumoPedidoRepository.countByStatus(StatusEnum.CONCLUIDO);
    }

    public long countPedidosAbertos() {
        return resumoPedidoRepository.countByStatusIn(List.of(StatusEnum.PENDENTE, StatusEnum.PAGO));
    }

    public long countPedidosTotal() {
        return resumoPedidoRepository.count();
    }

    public List<Map<String, Object>> getBolosMaisPedidos() {
        List<PedidoBolo> pedidosBolo = pedidoBoloRepository.findAll();

        Map<Integer, Long> contagemBolos = pedidosBolo.stream()
                .collect(Collectors.groupingBy(PedidoBolo::getBoloId, Collectors.counting()));

        return contagemBolos.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .map(entry -> {
                    Integer boloId = entry.getKey();
                    Long quantidade = entry.getValue();

                    Optional<Bolo> boloOpt = boloRepository.findById(boloId);

                    Double valorTotal = pedidosBolo.stream()
                            .filter(p -> p.getBoloId().equals(boloId))
                            .mapToDouble(p -> {
                                ResumoPedido resumoPedido = resumoPedidoRepository.findByPedidoBoloId(p.getId());
                                return resumoPedido != null ? resumoPedido.getValor() : 0.0;
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

                        nomeBolo =  decoracaoNome;
                    }

                    Map<String, Object> resultado = new HashMap<>();
                    resultado.put("boloId", boloId);
                    resultado.put("quantidade", quantidade);
                    resultado.put("nome", nomeBolo);
                    resultado.put("valorTotal", valorTotal);
                    return resultado;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getFornadasMaisPedidas() {
        List<PedidoFornada> pedidoFornadas = pedidoFornadaRepository.findAll();

        Map<Integer, Integer> quantidadePorFornadaDaVez = pedidoFornadas.stream()
                .collect(Collectors.groupingBy(
                        PedidoFornada::getFornadaDaVez,
                        Collectors.summingInt(PedidoFornada::getQuantidade)
                ));

        List<FornadaDaVez> fornadasDaVez = fornadaDaVezRepository.findAll();

        Map<Integer, Integer> quantidadePorProduto = new HashMap<>();
        Map<Integer, ProdutoFornada> produtoMap = new HashMap<>();
        Map<Integer, Double> valorTotalPorProduto = new HashMap<>();

        for (FornadaDaVez fdv : fornadasDaVez) {
            Integer qtdPedida = quantidadePorFornadaDaVez.getOrDefault(fdv.getId(), 0);
            Integer produtoId = fdv.getProdutoFornada();

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
                Double valorAtual = valorTotalPorProduto.getOrDefault(produtoId, 0.0);
                Double novoValor = valorAtual + (qtdPedida * produto.getValor());
                valorTotalPorProduto.put(produtoId, novoValor);
            }
        }

        return quantidadePorProduto.entrySet().stream()
                .map(entry -> {
                    Integer produtoId = entry.getKey();
                    ProdutoFornada produto = produtoMap.get(produtoId);

                    Map<String, Object> result = new HashMap<>();
                    result.put("produtoId", produtoId);
                    result.put("nomeProduto", produto != null ? produto.getProduto() : "Produto não encontrado");
                    result.put("quantidadeTotal", entry.getValue());
                    result.put("valorTotal", valorTotalPorProduto.getOrDefault(produtoId, 0.0));
                    return result;
                })
                .filter(map -> produtoMap.get(((Integer) map.get("produtoId"))) != null)
                .sorted((m1, m2) -> Integer.compare(
                        (Integer) m2.get("quantidadeTotal"),
                        (Integer) m1.get("quantidadeTotal")
                ))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getProdutosMaisPedidos() {

        List<Map<String, Object>> todasFornadas = getFornadasMaisPedidas();
        List<Map<String, Object>> todosBolos = getBolosMaisPedidos();

        List<Map<String, Object>> todosProdutos = new ArrayList<>();

        todasFornadas.forEach(produto -> {
            Map<String, Object> produtoUnificado = new HashMap<>();
            produtoUnificado.put("id", produto.get("produtoId"));
            produtoUnificado.put("nome", produto.get("nomeProduto"));
            produtoUnificado.put("quantidade", produto.get("quantidadeTotal"));
            produtoUnificado.put("valorTotal", produto.get("valorTotal"));
            produtoUnificado.put("tipo", "FORNADA");
            todosProdutos.add(produtoUnificado);
        });

        todosBolos.forEach(bolo -> {
            Map<String, Object> boloUnificado = new HashMap<>();
            boloUnificado.put("id", bolo.get("boloId"));
            boloUnificado.put("nome", bolo.get("nome"));
            boloUnificado.put("quantidade", bolo.get("quantidade"));
            boloUnificado.put("valorTotal", bolo.get("valorTotal"));
            boloUnificado.put("tipo", "BOLO");
            todosProdutos.add(boloUnificado);
        });

        return todosProdutos.stream()
                .sorted((p1, p2) -> Long.compare(
                        ((Number) p2.get("quantidade")).longValue(),
                        ((Number) p1.get("quantidade")).longValue()
                ))
                .collect(Collectors.toList());
    }
}
