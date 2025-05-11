package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.ResumoPedido;
import com.carambolos.carambolosapi.model.enums.StatusEnum;
import com.carambolos.carambolosapi.repository.PedidoBoloRepository;
import com.carambolos.carambolosapi.repository.PedidoFornadaRepository;
import com.carambolos.carambolosapi.repository.ResumoPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResumoPedidoService {

    @Autowired
    private ResumoPedidoRepository resumoPedidoRepository;

    @Autowired
    private PedidoBoloRepository pedidoBoloRepository;

    @Autowired
    private PedidoFornadaRepository pedidoFornadaRepository;

    public List<ResumoPedido> listarResumosPedidos() {
        return resumoPedidoRepository.findAllByIsAtivoTrue();
    }

    public ResumoPedido buscarResumoPedidoPorId(Integer id) {
        return resumoPedidoRepository.findByIdAndIsAtivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Resumo de pedido não encontrado"));
    }

    //LocalDateTime?
    public List<ResumoPedido> buscarResumosPedidosPorDataPedido(LocalDate dataPedido) {
        return resumoPedidoRepository.findByDataEntregaAndIsAtivoTrue(dataPedido);
    }

    public List<ResumoPedido> buscarResumosPedidosPorStatus(StatusEnum status) {
        return resumoPedidoRepository.findByStatusAndIsAtivoTrue(status);
    }

    public ResumoPedido cadastrarResumoPedido(ResumoPedido resumoPedido) {
        validarReferencias(resumoPedido);

        resumoPedido.setDataPedido(LocalDateTime.now());
        resumoPedido.setStatus(StatusEnum.PENDENTE);

        return resumoPedidoRepository.save(resumoPedido);
    }

    public ResumoPedido atualizarResumoPedido(Integer id, ResumoPedido resumoPedido) {
        if (!resumoPedidoRepository.existsByIdAndIsAtivoTrue(id)) {
            throw new EntidadeNaoEncontradaException("Resumo de pedido não encontrado");
        }
        validarReferencias(resumoPedido);
        resumoPedido.setId(id);
        return resumoPedidoRepository.save(resumoPedido);
    }

    public void deletarResumoPedido(Integer id) {
        ResumoPedido resumoPedido = buscarResumoPedidoPorId(id);
        resumoPedido.setAtivo(false);
        resumoPedidoRepository.save(resumoPedido);
    }

    public List<ResumoPedido> listarResumosPedidosBolo() {
        return resumoPedidoRepository.findByPedidoBoloIdIsNotNullAndIsAtivoTrue();
    }

    public List<ResumoPedido> listarResumosPedidosFornada() {
        return resumoPedidoRepository.findByPedidoFornadaIdIsNotNullAndIsAtivoTrue();
    }

    public ResumoPedido alterarStatus(Integer id, StatusEnum novoStatus) {
        ResumoPedido resumoPedido = buscarResumoPedidoPorId(id);
        StatusEnum statusAtual = resumoPedido.getStatus();
        if (!isTransicaoStatusValida(statusAtual, novoStatus)) {
            throw new EntidadeImprocessavelException("Não é possível alterar o status de %s para %s".formatted(statusAtual, novoStatus));
        }
        resumoPedido.setStatus(novoStatus);
        return resumoPedidoRepository.save(resumoPedido);
    }

    private void validarReferencias(ResumoPedido resumoPedido) {
        if (resumoPedido.getPedidoBoloId() == null && resumoPedido.getPedidoFornadaId() == null) {
            throw new IllegalArgumentException("O resumo de pedido deve estar vinculado a um pedido de bolo ou pedido de fornada");
        }

        if (resumoPedido.getPedidoBoloId() != null) {
            validarPedidoBolo(resumoPedido.getPedidoBoloId());
        }

        if(resumoPedido.getPedidoFornadaId() != null) {
            validarPedidoFornada(resumoPedido.getPedidoFornadaId());
        }
    }

    private void validarPedidoBolo(Integer pedidoBoloId) {
        if (!pedidoBoloRepository.existsByIdAndIsAtivoTrue(pedidoBoloId)) {
            throw new EntidadeNaoEncontradaException("Pedido de bolo com ID %d não encontrado".formatted(pedidoBoloId));
        }
    }

    private void validarPedidoFornada(Integer pedidoFornadaId) {
//        if (!pedidoFornadaRepository.existsByIdAndIsAtivoTrue(pedidoFornadaId)) {
//            throw new EntidadeNaoEncontradaException("Pedido de fornada com ID %d não encontrado".formatted(pedidoFornadaId));
//        }
    }

    private boolean isTransicaoStatusValida(StatusEnum statusAtual, StatusEnum novoStatus) {
        if (statusAtual == novoStatus) {
            return false;
        }

        return true;
    }


}
