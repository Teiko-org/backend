package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.response.DetalhePedidoBoloDTO;
import com.carambolos.carambolosapi.controller.response.EnderecoResponseDTO;
import com.carambolos.carambolosapi.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.*;
import com.carambolos.carambolosapi.model.enums.TipoEntregaEnum;
import com.carambolos.carambolosapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoBoloService {

    @Autowired
    private BoloRepository boloRepository;
    @Autowired
    PedidoBoloRepository pedidoBoloRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private MassaRepository massaRepository;
    @Autowired
    private RecheioPedidoRepository recheioPedidoRepository;
    @Autowired
    private RecheioUnitarioRepository recheioUnitarioRepository;
    @Autowired
    private CoberturaRepository coberturaRepository;
    @Autowired
    private RecheioExclusivoRepository recheioExclusivoRepository;

    public List<PedidoBolo> listarPedidos() {
        return pedidoBoloRepository.findAll().stream().filter(PedidoBolo::getAtivo).toList();
    }

    public PedidoBolo buscarPedidoPorId(Integer id) {
        return pedidoBoloRepository.findById(id)
                .filter(PedidoBolo::getAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido com o id %d não encontrado".formatted(id)));
    }

    public PedidoBolo cadastrarPedido(PedidoBolo pedidoBolo) {
        if (!boloRepository.existsByIdAndIsAtivoTrue(pedidoBolo.getBoloId())) {
            throw new EntidadeNaoEncontradaException("Bolo com id %d não encontrado".formatted(pedidoBolo.getBoloId()));
        }
        if (pedidoBolo.getTipoEntrega() == TipoEntregaEnum.ENTREGA && pedidoBolo.getEnderecoId() == null) {
            throw new EntidadeImprocessavelException("Tipo de entrega 'ENTREGA' requer um endereço válido.");
        }
        if (pedidoBolo.getEnderecoId() != null && !enderecoRepository.existsByIdAndIsAtivoTrue(pedidoBolo.getEnderecoId())) {
            throw new EntidadeNaoEncontradaException("Endereço com id %d não encontrado".formatted(pedidoBolo.getEnderecoId()));
        }

        pedidoBolo.setDataUltimaAtualizacao(LocalDateTime.now());

        return pedidoBoloRepository.save(pedidoBolo);
    }

    public PedidoBolo atualizarPedido(PedidoBolo pedidoBolo, Integer id) {
        if (!pedidoBoloRepository.existsByIdAndIsAtivoTrue(id)) {
            throw new EntidadeNaoEncontradaException("Pedido com id %d não encontrado".formatted(id));
        }
        if (pedidoBolo.getTipoEntrega() == TipoEntregaEnum.ENTREGA && pedidoBolo.getEnderecoId() == null) {
            throw new EntidadeImprocessavelException("Tipo de entrega 'ENTREGA' requer um endereço válido.");
        }
        if (!boloRepository.existsByIdAndIsAtivoTrue(pedidoBolo.getBoloId())) {
            throw new EntidadeNaoEncontradaException("Bolo com id %d não encontrado".formatted(pedidoBolo.getBoloId()));
        }
        if (pedidoBolo.getEnderecoId() != null && !enderecoRepository.existsByIdAndIsAtivoTrue(pedidoBolo.getEnderecoId())) {
            throw new EntidadeNaoEncontradaException("Endereço com id %d não encontrado".formatted(pedidoBolo.getEnderecoId()));
        }

        pedidoBolo.setDataUltimaAtualizacao(LocalDateTime.now());
        pedidoBolo.setId(id);

        return pedidoBoloRepository.save(pedidoBolo);
    }

    public void deletarPedido(Integer id) {
        PedidoBolo pedido = buscarPedidoPorId(id);
        pedido.setAtivo(false);
        pedidoBoloRepository.save(pedido);
    }

    public DetalhePedidoBoloDTO obterDetalhePedido(Integer pedidoId) {
        PedidoBolo pedido = pedidoBoloRepository.findById(pedidoId)
                .filter(PedidoBolo::getAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido com o id %d não encontrado".formatted(pedidoId)));

        Bolo bolo = boloRepository.findById(pedido.getBoloId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Bolo com id %d não encontrado".formatted(pedido.getBoloId())));

        String massaNome = massaRepository.findById(bolo.getMassa())
                .map(Massa::getSabor)
                .orElse("Não especificada");

        String recheioNome = recheioPedidoRepository.findById(bolo.getRecheioPedido())
                .map(recheioPedido -> {
                    StringBuilder nomes = new StringBuilder();

                    if (recheioPedido.getRecheioExclusivo() != null) {
                        return recheioExclusivoRepository.buscarRecheioExclusivoPorId(recheioPedido.getRecheioExclusivo())
                                .getNome() + " (" +
                                recheioExclusivoRepository.buscarRecheioExclusivoPorId(recheioPedido.getRecheioExclusivo()).getSabor1() +
                                " + " +
                                recheioExclusivoRepository.buscarRecheioExclusivoPorId(recheioPedido.getRecheioExclusivo()).getSabor2() +
                                ")";
                    } else {
                        if (recheioPedido.getRecheioUnitarioId1() != null) {
                            recheioUnitarioRepository.findById(recheioPedido.getRecheioUnitarioId1())
                                    .ifPresent(recheio -> nomes.append(recheio.getSabor()));
                        }

                        if (recheioPedido.getRecheioUnitarioId2() != null) {
                            if (!nomes.isEmpty()) {
                                nomes.append(" + ");
                            }
                            recheioUnitarioRepository.findById(recheioPedido.getRecheioUnitarioId2())
                                    .ifPresent(recheio -> nomes.append(recheio.getSabor()));
                        }
                        return nomes.toString();
                    }
                })
                .orElse("Não especificado");


        String coberturaNome = coberturaRepository.findById(bolo.getCobertura())
                .map(Cobertura::getDescricao)
                .orElse("Não especificada");

        //TODO: Implementar lógica para buscar imagens de decoração
        String decoracao = "Nenhuma imagem de referência adicionada";

        EnderecoResponseDTO enderecoDTO = null;
        if (pedido.getTipoEntrega() == TipoEntregaEnum.ENTREGA && pedido.getEnderecoId() != null) {
            enderecoDTO = enderecoRepository.findById(pedido.getEnderecoId())
                    .filter(Endereco::isAtivo)
                    .map(EnderecoResponseDTO::toResponseDTO)
                    .orElse(null);
        }

        return DetalhePedidoBoloDTO.toDetalhePedidoResponse(
                pedido.getId(),
                bolo.getTamanho(),
                bolo.getFormato(),
                massaNome,
                recheioNome,
                coberturaNome,
                decoracao,
                pedido.getObservacao(),
                pedido.getTipoEntrega(),
                pedido.getDataUltimaAtualizacao(),
                pedido.getNomeCliente(),
                pedido.getTelefoneCliente(),
                enderecoDTO
        );
    }
}
