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

    public DashboardService(PedidoBoloRepository pedidoBoloRepository,
                            UsuarioRepository usuarioRepository,
                            BoloRepository boloRepository,
                            PedidoFornadaRepository pedidoFornadaRepository,
                            MassaRepository massaRepository,
                            RecheioPedidoRepository recheioPedidoRepository,
                            RecheioUnitarioRepository recheioUnitarioRepository,
                            ProdutoFornadaRepository produtoFornadaRepository,
                            FornadaDaVezRepository fornadaDaVezRepository, ResumoPedidoRepository resumoPedidoRepository, DecoracaoRepository decoracaoRepository) {
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

    public List<Map<String, Object>> getBolosMaisPedidos(int limit) {
        List<PedidoBolo> pedidosBolo = pedidoBoloRepository.findAll();

        Map<Integer, Long> contagemBolos = pedidosBolo.stream()
                .collect(Collectors.groupingBy(PedidoBolo::getBoloId, Collectors.counting()));

        return contagemBolos.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    Optional<Bolo> bolo = boloRepository.findById(entry.getKey());

                    Double valorTotal = pedidosBolo.stream()
                            .filter(p -> p.getBoloId().equals(entry.getKey()))
                            .mapToDouble(p -> {
                                ResumoPedido resumoPedido = resumoPedidoRepository.findByPedidoBoloId(p.getId());
                                return resumoPedido != null ? resumoPedido.getValor() : 0.0;
                            })
                            .sum();

                    String nomeDecoracao = bolo
                            .flatMap(b -> decoracaoRepository.findById(b.getDecoracao()))
                            .map(Decoracao::getNome)
                            .orElse("Desconhecido");

                    Map<String, Object> resultado = new HashMap<>();
                    resultado.put("boloId", entry.getKey());
                    resultado.put("quantidade", entry.getValue());
                    resultado.put("nome", nomeDecoracao);
                    resultado.put("valorTotal", valorTotal);
                    return resultado;
                })
                .collect(Collectors.toList());
    }

}
