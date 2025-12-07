package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class EnderecoUseCase {
    private final EnderecoGateway enderecoGateway;
    private final UsuarioUseCase usuarioUseCase;

    public EnderecoUseCase(EnderecoGateway enderecoGateway, UsuarioUseCase usuarioUseCase) {
        this.enderecoGateway = enderecoGateway;
        this.usuarioUseCase = usuarioUseCase;
    }

    public Page<Endereco> listar(Pageable pageable) {
        return enderecoGateway.listar(pageable);
    }

    @Cacheable(cacheNames = "enderecos:porUsuario", key = "#usuarioId")
    public List<Endereco> listarPorUsuario(Integer usuarioId) {
        return enderecoGateway.listarPorUsuario(usuarioId);
    }

    @Cacheable(cacheNames = "enderecos:porId", key = "#id")
    public Endereco buscarPorId(Integer id) {
        Endereco endereco = enderecoGateway.buscarPorId(id);
        if (endereco == null) {
            throw new EntidadeNaoEncontradaException("Endereço com Id %d não encontrado.".formatted(id));
        }
        return endereco;
    }

    @CacheEvict(cacheNames = {"enderecos:porUsuario", "enderecos:porId"}, allEntries = true)
    public Endereco cadastrar(Endereco endereco) {
        if (endereco.getUsuario() != null) {
            if (enderecoGateway.existeEnderecoDuplicado(endereco) && endereco.isAtivo()) {
                throw new EntidadeJaExisteException("Endereço já cadastrado");
            }

            Usuario usuarioFk = usuarioUseCase.buscarPorId(endereco.getUsuario());
            if (!endereco.getUsuario().equals(usuarioFk.getId())) {
                throw new EntidadeNaoEncontradaException("Usuariofk não existe no banco");
            }
        }
        Endereco enderecoSalvo = enderecoGateway.cadastrar(endereco);
        return enderecoSalvo;
    }

    @CacheEvict(cacheNames = {"enderecos:porUsuario", "enderecos:porId"}, allEntries = true)
    public Endereco atualizar(Integer id, Endereco endereco) {
        Endereco enderecoExistente = enderecoGateway.buscarPorId(id);
        if (enderecoExistente == null) {
            throw new EntidadeNaoEncontradaException("Endereço com Id %d não encontrado.".formatted(id));
        }

        if (enderecoGateway.existeEnderecoDuplicadoParaAtualizacao(endereco, id)) {
            throw new EntidadeJaExisteException("Endereço já cadastrado");
        }

        Endereco enderecoAtualizado = enderecoGateway.atualizar(id, endereco);
        return enderecoAtualizado;
    }

    @CacheEvict(cacheNames = {"enderecos:porUsuario", "enderecos:porId"}, allEntries = true)
    public void deletar(Integer id) {
        Endereco endereco = enderecoGateway.buscarPorId(id);
        if (endereco == null) {
            throw new EntidadeNaoEncontradaException("Endereço com Id %d não encontrado.".formatted(id));
        }
        enderecoGateway.deletar(id);
    }
}
