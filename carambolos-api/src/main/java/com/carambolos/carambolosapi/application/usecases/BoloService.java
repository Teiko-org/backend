package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.domain.entity.*;
import com.carambolos.carambolosapi.application.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.domain.projection.DetalheBoloProjection;
import com.carambolos.carambolosapi.domain.projection.RecheioExclusivoProjection;
import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.domain.projection.RecheioPedidoProjection;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioPedidoEntity;
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

    public RecheioExclusivoProjection cadastrarRecheioExclusivo(RecheioExclusivo recheioExclusivo) {
        int id1 = recheioExclusivo.getRecheioUnitarioId1();
        int id2 = recheioExclusivo.getRecheioUnitarioId2();

        boolean recheiosExistem = recheioUnitarioRepository.existsByIdAndIsAtivoTrue(id1) && recheioUnitarioRepository.existsByIdAndIsAtivoTrue(id2);
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
        return recheioExclusivoRepository.listarRecheiosExclusivos().stream()
                .filter(recheioExclusivo -> recheioExclusivo.getIsAtivo() != null && recheioExclusivo.getIsAtivo() == 1)
                .toList();
    }

    public RecheioExclusivoProjection editarRecheioExclusivo(RecheioExclusivo recheioExclusivo, Integer id) {
        RecheioExclusivo recheioExistente = recheioExclusivoRepository.findById(id)
                .filter(RecheioExclusivo::getAtivo)
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

    public void excluirRecheioExclusivo(Integer id) {
        Optional<RecheioExclusivo> possivelRecheio = recheioExclusivoRepository.findById(id)
                .filter(RecheioExclusivo::getAtivo);
        if (possivelRecheio.isPresent()) {
            RecheioExclusivo recheio = possivelRecheio.get();
            recheio.setAtivo(false);
            recheioExclusivoRepository.save(recheio);
            return;
        }

        throw new EntidadeNaoEncontradaException("Recheio exclusivo com id %s não encontrado".formatted(id));
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


}
