package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.domain.entity.*;
import com.carambolos.carambolosapi.application.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.domain.projection.DetalheBoloProjection;
import com.carambolos.carambolosapi.domain.projection.RecheioExclusivoProjection;
import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.domain.projection.RecheioPedidoProjection;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioExclusivoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoloService {

    @Autowired
    private BoloRepository boloRepository;

    @Autowired
    private RecheioUnitarioRepository recheioUnitarioRepository;

    @Autowired
    private RecheioExclusivoRepository recheioExclusivoRepository;

    @Autowired
    private RecheioPedidoRepository recheioPedidoRepository;

    @Autowired
    CoberturaRepository coberturaRepository;

    @Autowired
    MassaRepository massaRepository;

    @Autowired
    DecoracaoRepository decoracaoRepository;


    public List<Bolo> listarBolos(List<String> categorias) {
        List<Bolo> bolos;

        if (!categorias.isEmpty()) {
            bolos = boloRepository.findByCategoriaIn(categorias);
            bolos = bolos.stream().filter(Bolo::getAtivo).toList();
        } else {
            bolos =   boloRepository.findAll().stream().filter(Bolo::getAtivo).toList();
        }
        return bolos;
    }

    public List<DetalheBoloProjection> listarDetalhesBolos() {
        return boloRepository.listarDetalheBolo();
    }

    public Bolo buscarBoloPorId(Integer id) {
        return boloRepository.findById(id)
                .filter(Bolo::getAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Bolo com o id %d não encontrado".formatted(id)));
    }

    public Bolo cadastrarBolo(Bolo bolo) {
        // Para novos bolos, o ID será null ou 0, então não precisamos verificar se já existe
        if (bolo.getId() != null && bolo.getId() > 0) {
            boolean existeBoloAtivo = boloRepository.existsByIdAndIsAtivoTrue(bolo.getId());
            if (existeBoloAtivo) {
                throw new EntidadeJaExisteException("Esse bolo já existe no banco de dados.");
            }
        }

        if (!massaRepository.existsByIdAndIsAtivo(bolo.getMassa(), true)) {
            throw new EntidadeNaoEncontradaException("Essa massa não existe");
        }
        if (!recheioPedidoRepository.existsByIdAndIsAtivoTrue(bolo.getRecheioPedido())) {
            throw new EntidadeNaoEncontradaException("Esse recheio não existe");
        }
        if (!coberturaRepository.existsByIdAndIsAtivoTrue(bolo.getCobertura())) {
            throw new EntidadeNaoEncontradaException("Essa cobertura não existe");
        }
        if (bolo.getDecoracao() != null && !decoracaoRepository.existsByIdAndIsAtivoTrue(bolo.getDecoracao())) {
            throw new EntidadeNaoEncontradaException("Essa decoração não existe");
        }

        return boloRepository.save(bolo);
    }

    public Bolo atualizarBolo(Bolo bolo, Integer id) {
        boloRepository.findById(id)
                .filter(Bolo::getAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Bolo com o id %d não encontrado".formatted(id)));

        if (boloRepository.existsByIdAndIdNotAndIsAtivoTrue(id, bolo.getId())) {
            throw new EntidadeJaExisteException("Esse bolo já existe no banco de dados.");
        }

        if (!massaRepository.existsByIdAndIsAtivo(bolo.getMassa(), true)) {
            throw new EntidadeNaoEncontradaException("Essa massa não existe");
        }
        if (!recheioPedidoRepository.existsByIdAndIsAtivoTrue(bolo.getRecheioPedido())) {
            throw new EntidadeNaoEncontradaException("Esse recheio não existe");
        }
        if (!coberturaRepository.existsByIdAndIsAtivoTrue(bolo.getCobertura())) {
            throw new EntidadeNaoEncontradaException("Essa cobertura não existe");
        }
        if (bolo.getDecoracao() != null && !decoracaoRepository.existsByIdAndIsAtivoTrue(bolo.getDecoracao())) {
            throw new EntidadeNaoEncontradaException("Essa decoração não existe");
        }

        bolo.setId(id);
        return boloRepository.save(bolo);
    }

    public void atualizarStatusBolo(Boolean status, Integer id) {
        Integer statusInt = status ? 1 : 0;

        if (boloRepository.existsById(id)) {
            boloRepository.atualizarStatusBolo(statusInt, id);
        } else {
            throw new EntidadeNaoEncontradaException("Bolo com id %d não encontrado".formatted(id));
        }
    }

    public void deletarBolo(Integer id) {
        Optional<Bolo> possivelBolo = boloRepository.findById(id)
                .filter(Bolo::getAtivo);
        if (possivelBolo.isPresent()) {
            Bolo bolo = possivelBolo.get();
            bolo.setAtivo(false);
            boloRepository.save(bolo);
            return;
        }
        throw new EntidadeNaoEncontradaException("Bolo com id %d não encontrado".formatted(id));
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

    public RecheioPedidoProjection atualizarRecheioPedido(RecheioPedido recheioPedido, Integer id) {
        verificarCampos(recheioPedido);

        if (recheioPedido.getRecheioExclusivo() != null) {
            RecheioPedido recheioSalvo = recheioPedidoRepository.save(recheioPedido);
            return recheioPedidoRepository.buscarRecheioPedidoExclusivoPorId(recheioSalvo.getId());
        }
        recheioPedido.setId(id);
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
        return recheioPedidoRepository.listarRecheiosPedido().stream()
                .filter(recheioPedido -> Boolean.TRUE.equals(recheioPedido.getIsAtivo()))
                .toList();
    }

    public void deletarRecheioPedido(Integer id) {
        Optional<RecheioPedido> possivelRecheioPedido = recheioPedidoRepository.findById(id)
                .filter(RecheioPedido::getAtivo);

        if (possivelRecheioPedido.isPresent()) {
            RecheioPedido recheioPedido = possivelRecheioPedido.get();
            recheioPedido.setAtivo(false);
            recheioPedidoRepository.save(recheioPedido);
            return;
        }

        throw new EntidadeNaoEncontradaException("Recheio exclusivo com id %s não encontrado".formatted(id));
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
