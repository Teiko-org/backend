package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.model.*;
import com.carambolos.carambolosapi.model.projection.DetalheBoloProjection;
import com.carambolos.carambolosapi.model.projection.RecheioExclusivoProjection;
import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.projection.RecheioPedidoProjection;
import com.carambolos.carambolosapi.repository.*;
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

    public RecheioUnitario cadastrarRecheioUnitario(RecheioUnitario recheioUnitario) {
        if (recheioUnitarioRepository.countBySaborIgnoreCaseAndIsAtivo(recheioUnitario.getSabor(), true) > 0) {
            throw new EntidadeJaExisteException("Recheio com o sabor %s já existe".formatted(recheioUnitario.getSabor()));
        }
        return recheioUnitarioRepository.save(recheioUnitario);
    }

    public List<RecheioUnitario> listarRecheiosUnitarios() {
        return recheioUnitarioRepository.findAll().stream().filter(RecheioUnitario::getAtivo).toList();
    }

    public RecheioUnitario buscarPorId(Integer id) {
        return recheioUnitarioRepository.findById(id)
                .filter(RecheioUnitario::getAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio com o id %d não encontrado".formatted(id)));
    }

    public RecheioUnitario atualizarRecheioUnitario(RecheioUnitario recheioUnitario, Integer id) {
        RecheioUnitario recheioExistente = recheioUnitarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio com o id %d não encontrado".formatted(id)));

        if (recheioUnitarioRepository.countBySaborIgnoreCaseAndIdNotAndIsAtivo(recheioUnitario.getSabor(), id, true) > 0) {
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
        Optional<RecheioUnitario> possivelRecheio = recheioUnitarioRepository.findById(id)
                .filter(RecheioUnitario::getAtivo);
        if (possivelRecheio.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Recheio com id %d não existe".formatted(id));
        }
        RecheioUnitario recheio = possivelRecheio.get();
        recheio.setAtivo(false);
        recheioUnitarioRepository.save(recheio);
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

    public Cobertura cadastrarCobertura(Cobertura cobertura) {
        String cor = cobertura.getCor();
        String descricao = cobertura.getDescricao();
        Integer coberturasExistentes = coberturaRepository.countByCorAndDescricaoIgnoreCaseAndIsAtivoTrue(cor, descricao);

        if (coberturasExistentes > 0) {
            throw new EntidadeJaExisteException("Cobertura com a cor %s e descricao %s ja existente".formatted(cor, descricao));
        }

        return coberturaRepository.save(cobertura);
    }

    public Cobertura atualizarCobertura(Cobertura novaCobertura, Integer id) {
        Optional<Cobertura> possivelCobertura = coberturaRepository.findById(id)
                .filter(Cobertura::getAtivo);
        if (possivelCobertura.isPresent()) {
            String cor = novaCobertura.getCor();
            String descricao = novaCobertura.getDescricao();

            Integer coberturasExistentes = coberturaRepository.countByCorAndDescricaoAndIdNotAndIsAtivoTrue(
                    cor,
                    descricao,
                    id
            );

            if (coberturasExistentes > 0) {
                throw new EntidadeJaExisteException("Cobertura com cor %s e descricao %s ja existe".formatted(cor, descricao));
            }

            Cobertura cobertura = possivelCobertura.get();
            return coberturaRepository.save(
                    verificarCampos(cobertura, novaCobertura)
            );
        }

        throw new EntidadeNaoEncontradaException("Cobertura com id %d não encontrada".formatted(id));
    }

    public List<Cobertura> listarCoberturas() {
        return coberturaRepository.findAll().stream().filter(Cobertura::getAtivo).toList();
    }

    public Cobertura buscarCoberturaPorId(Integer id) {
        Optional<Cobertura> possivelCobertura = coberturaRepository.findById(id)
                .filter(Cobertura::getAtivo);

        if (possivelCobertura.isEmpty()) {
            throw new EntidadeJaExisteException("Cobertura com id %d não encontrar".formatted(id));
        }

        return possivelCobertura.get();
    }

    public void deletarCobertura(Integer id) {
        Optional<Cobertura> possivelCobertura = coberturaRepository.findById(id)
                .filter(Cobertura::getAtivo);
        if (possivelCobertura.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Cobertura com o id %d não encontrada".formatted(id));
        }

        Cobertura cobertura = possivelCobertura.get();
        cobertura.setAtivo(false);
        coberturaRepository.save(cobertura);
    }

    public Massa cadastrarMassa(Massa massa) {
        if (massaRepository.countBySaborAndIsAtivo(massa.getSabor(), true) > 0) {
            throw new EntidadeJaExisteException("Massa com sabor %s já existente".formatted(massa.getSabor()));
        }
        return massaRepository.save(massa);
    }

    public Massa atualizarMassa(Massa massa, Integer id) {
        if (!massaRepository.existsByIdAndIsAtivo(id, true)) {
            throw new EntidadeNaoEncontradaException("Massa com id %d não existente".formatted(id));
        }
        if (massaRepository.countBySaborAndIdNotAndIsAtivo(massa.getSabor(), id, true) > 0) {
            throw new EntidadeJaExisteException("Massa com saber %s ja existente".formatted(massa.getSabor()));
        }
        massa.setId(id);
        return massaRepository.save(massa);
    }

    public List<Massa> listarMassas() {
        return massaRepository.findAll().stream().filter(Massa::getAtivo).toList();
    }

    public Massa buscarMassaPorId(Integer id) {
        Optional<Massa> possivelMassa = massaRepository.findById(id)
                .filter(Massa::getAtivo);
        if (possivelMassa.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Massa com id %d não encontrada".formatted(id));
        }

        return possivelMassa.get();
    }

    public void deletarMassa(Integer id) {
        Optional<Massa> possivelMassa = massaRepository.findById(id)
                .filter(Massa::getAtivo);
        if (possivelMassa.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Massa com id %d não encontrada".formatted(id));
        }
        Massa massa = possivelMassa.get();
        massa.setAtivo(false);
        massaRepository.save(massa);
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

    private Cobertura verificarCampos(Cobertura cobertura, Cobertura novaCobertura) {
        if (novaCobertura.getCor() == null) {
            novaCobertura.setCor(cobertura.getCor());
        }

        if (novaCobertura.getDescricao() == null) {
            novaCobertura.setDescricao(cobertura.getDescricao());
        }
        novaCobertura.setId(cobertura.getId());
        return novaCobertura;
    }
}
