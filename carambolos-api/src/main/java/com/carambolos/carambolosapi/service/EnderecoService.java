package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.repository.EnderecoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.carambolos.carambolosapi.utils.EnderecoHasher;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final UsuarioService usuarioService;

    public EnderecoService(EnderecoRepository enderecoRepository, UsuarioService usuarioService) {
        this.enderecoRepository = enderecoRepository;
        this.usuarioService = usuarioService;
    }

    public List<Endereco> listar() {
        return enderecoRepository.findAllByIsAtivoTrue();
    }

    public List<Endereco> listarPorUsuario(Integer usuarioId) {
        return enderecoRepository.findByUsuarioAndIsAtivoTrue(usuarioId);
    }

    public Endereco buscarPorId(Integer id) {
        Endereco enderecoExistente = enderecoRepository.findByIdAndIsAtivoTrue(id);
        if (enderecoExistente != null) {
            return enderecoExistente;
        }
        throw new EntidadeNaoEncontradaException("Endereço com Id %d não encontrado.".formatted(id));
    }

    public Endereco cadastrar(Endereco endereco) {
        preencherDedupHash(endereco);
        if (endereco.getUsuario() != null) {
            if (existeEnderecoDuplicado(endereco) && endereco.isAtivo()) {
                throw new EntidadeJaExisteException("Endereço já cadastrado");
            }

            Usuario usuarioFk = usuarioService.buscarPorId(endereco.getUsuario());

            if (!endereco.getUsuario().equals(usuarioFk.getId())) {
                throw new EntidadeNaoEncontradaException("Usuariofk não existe no banco");
            }
        }
        return enderecoRepository.save(endereco);
    }

    public Endereco atualizar(Integer id, Endereco endereco) {
        if (!enderecoRepository.existsByIdAndIsAtivoTrue(id)) {
            throw new EntidadeNaoEncontradaException(("Endereço com Id %d não encontrado.".formatted(id)));
        }
        preencherDedupHash(endereco);
        if (existeEnderecoDuplicadoParaAtualizacao(endereco, id)) {
            throw new EntidadeJaExisteException("Endereço já cadastrado");
        }
        endereco.setId(id);
        return enderecoRepository.save(endereco);
    }

    public void deletar(Integer id) {
        Endereco endereco = enderecoRepository.findByIdAndIsAtivoTrue(id);
        if (endereco != null) {
            endereco.setAtivo(false);
            enderecoRepository.save(endereco);
            return;
        }
        throw new EntidadeNaoEncontradaException("Endereço com Id %d não encontrado.".formatted(id));
    }

    public Boolean existeEnderecoDuplicado(Endereco endereco) {
        if (endereco.getUsuario() == null) {
            return false;
        }
        return enderecoRepository.countByUsuarioAndDedupHashAndIsAtivoEquals(
                endereco.getUsuario(), endereco.getDedupHash(), true) > 0;
    }

    public Boolean existeEnderecoDuplicadoParaAtualizacao(Endereco endereco, Integer id) {
        if (endereco.getUsuario() == null) {
            return false;
        }
        return enderecoRepository.countByUsuarioAndDedupHashAndIsAtivoEqualsAndIdNot(
                endereco.getUsuario(), endereco.getDedupHash(), true, id) > 0;
    }

    private void preencherDedupHash(Endereco e) { e.setDedupHash(EnderecoHasher.computeDedupHash(e)); }


}
