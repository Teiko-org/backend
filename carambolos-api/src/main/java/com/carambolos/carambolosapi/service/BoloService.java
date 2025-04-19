package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.model.projection.RecheioExclusivoProjection;
import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.RecheioExclusivo;
import com.carambolos.carambolosapi.model.RecheioPedido;
import com.carambolos.carambolosapi.model.RecheioUnitario;
import com.carambolos.carambolosapi.model.projection.RecheioPedidoProjection;
import com.carambolos.carambolosapi.repository.RecheioExclusivoRepository;
import com.carambolos.carambolosapi.repository.RecheioPedidoRepository;
import com.carambolos.carambolosapi.repository.RecheioUnitarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoloService {
    @Autowired
    private RecheioUnitarioRepository recheioUnitarioRepository;

    @Autowired
    private RecheioExclusivoRepository recheioExclusivoRepository;

    @Autowired
    private RecheioPedidoRepository recheioPedidoRepository;

    public RecheioUnitario cadastrarRecheioUnitario(RecheioUnitario recheioUnitario) {
        if (recheioUnitarioRepository.countBySaborIgnoreCase(recheioUnitario.getSabor()) > 0) {
            throw new EntidadeJaExisteException("Recheio com o sabor %s já existe".formatted(recheioUnitario.getSabor()));
        }
        return recheioUnitarioRepository.save(recheioUnitario);
    }

    public List<RecheioUnitario> listarRecheiosUnitarios() {
        return recheioUnitarioRepository.findAll();
    }

    public RecheioUnitario buscarPorId(Integer id) {
        return recheioUnitarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio com o id %d não encontrado".formatted(id)));
    }

    public RecheioUnitario atualizarRecheioUnitario(RecheioUnitario recheioUnitario, Integer id) {
        RecheioUnitario recheioExistente = recheioUnitarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio com o id %d não encontrado".formatted(id)));

        if (recheioUnitarioRepository.countBySaborIgnoreCaseAndIdNot(recheioUnitario.getSabor(), id) > 0) {
            throw new EntidadeJaExisteException("Recheio com o sabor %s já existe".formatted(recheioUnitario.getSabor()));
        }

        if (recheioUnitario.getSabor() != null) {
            recheioExistente.setSabor(recheioUnitario.getSabor());
        }
        if (recheioUnitario.getDescricao() != null) {
            recheioExistente.setDescricao(recheioUnitario.getDescricao());
        }
        if (recheioUnitario.getValor() != null) {
            recheioExistente.setValor(recheioUnitario.getValor());
        }

        recheioExistente.setId(id);

        return recheioUnitarioRepository.save(recheioExistente);
    }

    public void deletarRecheioUnitario(Integer id) {
        if (!recheioUnitarioRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Recheio com id %d não existe".formatted(id));
        }
        recheioUnitarioRepository.deleteById(id);
    }

    public RecheioExclusivoProjection cadastrarRecheioExclusivo(RecheioExclusivo recheioExclusivo) {
        int id1 = recheioExclusivo.getRecheioUnitarioId1();
        int id2 = recheioExclusivo.getRecheioUnitarioId2();

        boolean recheiosExistem = recheioUnitarioRepository.existsById(id1) && recheioUnitarioRepository.existsById(id2);
        if (!recheiosExistem) {
            throw new EntidadeNaoEncontradaException("Um ou mais recheios cadastrados não existem");
        }

        Integer recheiosExistentes1 = recheioExclusivoRepository.countByRecheioUnitarioIds(id1, id2);
        Integer recheiosExistentes2 = recheioExclusivoRepository.countByRecheioUnitarioIds(id2, id1);

        if (recheiosExistentes1 > 0 || recheiosExistentes2 > 0) {
            throw new EntidadeJaExisteException("Recheio exclusivo já existente");
        }

        RecheioExclusivo recheioSalvo = recheioExclusivoRepository.save(recheioExclusivo);
        return recheioExclusivoRepository.buscarRecheioExclusivoPorId(recheioSalvo.getId());
    }

    public RecheioExclusivoProjection buscarRecheioExclusivoPorId(Integer id) {
        return recheioExclusivoRepository.buscarRecheioExclusivoPorId(id);
    }

    public List<RecheioExclusivoProjection> listarRecheiosExclusivos() {
        return recheioExclusivoRepository.listarRecheiosExclusivos();
    }

    public RecheioExclusivoProjection editarRecheioExclusivo(RecheioExclusivo recheioExclusivo, Integer id) {
        RecheioExclusivo recheioExistente = recheioExclusivoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio com o id %d não encontrado".formatted(id)));

        Integer id1 = recheioExclusivo.getRecheioUnitarioId1();
        Integer id2 = recheioExclusivo.getRecheioUnitarioId2();
        Integer recheiosExistentes1 = recheioExclusivoRepository.countByRecheioUnitarioIds(id1, id2);
        Integer recheiosExistentes2 = recheioExclusivoRepository.countByRecheioUnitarioIds(id2, id1);

        if (recheiosExistentes1 > 0 || recheiosExistentes2 > 0) {
            throw new EntidadeJaExisteException("Recheio exclusivo já existente");
        }

        recheioExistente = verificarCampos(recheioExclusivo, recheioExistente);
        recheioExistente.setId(id);
        recheioExclusivoRepository.save(recheioExistente);

        return buscarRecheioExclusivoPorId(id);
    }

    public Void excluirRecheioExclusivo(Integer id) {
        if (recheioExclusivoRepository.existsById(id)) {
            recheioExclusivoRepository.deleteById(id);
            return null;
        }
        throw new EntidadeNaoEncontradaException("Recheio exclusivo com id %s não encontrado".formatted(id));
    }

    public RecheioPedidoProjection cadastrarRecheioPedido(RecheioPedido recheioPedido) {
        verificarCampos(recheioPedido);

        if (recheioPedido.getRecheioExclusivo() != null) {
            RecheioPedido recheioSalvo = recheioPedidoRepository.save(recheioPedido);
            return recheioPedidoRepository.buscarRecheioPedidoExclusivoPorId(recheioSalvo.getId());
        }
        RecheioPedido recheioSalvo = recheioPedidoRepository.save(recheioPedido);
        return recheioPedidoRepository.buscarRecheioPedidoUnitariosPorId(recheioSalvo.getId());
    }

    public RecheioPedidoProjection buscarRecheioPedidoPorId(Integer id) {
        if (!recheioPedidoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Recheio do pedido com id %d não encontrado".formatted(id));
        }
        Optional<RecheioPedido> possivelRecheio = recheioPedidoRepository.findById(id);

        if (possivelRecheio.isPresent()) {
            RecheioPedido recheioPedido = possivelRecheio.get();
            verificarCampos(recheioPedido);
            if (recheioPedido.getRecheioExclusivo() != null) {
                return recheioPedidoRepository.buscarRecheioPedidoExclusivoPorId(recheioPedido.getId());
            }
            return recheioPedidoRepository.buscarRecheioPedidoUnitariosPorId(recheioPedido.getId());
        }
        throw new EntidadeImprocessavelException("Falha ao buscar recheio do pedido");
    }

    public List<RecheioPedidoProjection> listarRecheiosPedido() {
        return recheioPedidoRepository.listarRecheiosPedido();
    }

    private RecheioExclusivo verificarCampos(RecheioExclusivo recheioExclusivo, RecheioExclusivo recheioExistente) {
        if (recheioExclusivo.getNome() != null) {
            recheioExistente.setNome(recheioExclusivo.getNome());
        }
        if (recheioExclusivo.getRecheioUnitarioId1() != null) {
            recheioExistente.setRecheioUnitarioId1(recheioExclusivo.getRecheioUnitarioId1());
        }
        if (recheioExclusivo.getRecheioUnitarioId2() != null) {
            recheioExistente.setRecheioUnitarioId2(recheioExclusivo.getRecheioUnitarioId2());
        }
        return recheioExistente;
    }

    private void verificarCampos(RecheioPedido recheioPedido) {
        if (recheioPedido.getRecheioUnitarioId1() != null && recheioPedido.getRecheioUnitarioId2() != null && recheioPedido.getRecheioExclusivo() != null) {
            throw new EntidadeImprocessavelException("Requisição apenas pode ter o recheio exclusivo nulo ou recheios unitarios nulos");
        } else if (
                recheioPedido.getRecheioUnitarioId1() == null && recheioPedido.getRecheioUnitarioId2() != null ||
                        recheioPedido.getRecheioUnitarioId1() != null && recheioPedido.getRecheioUnitarioId2() == null
        ) {
            throw new EntidadeImprocessavelException("Requisição apenas pode ter o recheio exclusivo nulo ou recheios unitarios nulos");
        }
    }
}
