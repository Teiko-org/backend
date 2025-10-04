package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.application.usecases.UsuarioUseCase;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.EnderecoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.EnderecoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository;
import com.carambolos.carambolosapi.system.security.EnderecoHasher;

import java.util.List;

public class EnderecoGatewayImpl implements EnderecoGateway {
    private final EnderecoRepository enderecoRepository;
    private final EnderecoMapper enderecoMapper;
    private final UsuarioUseCase usuarioUseCase;

    public EnderecoGatewayImpl(EnderecoRepository enderecoRepository, EnderecoMapper enderecoMapper, UsuarioUseCase usuarioUseCase) {
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
        this.usuarioUseCase = usuarioUseCase;
    }

    @Override
    public List<Endereco> listar() {
        List<EnderecoEntity> enderecos = enderecoRepository.findAllByIsAtivoTrue();
        return enderecoMapper.toDomain(enderecos);
    }

    @Override
    public List<Endereco> listarPorUsuario(Integer usuarioId) {
        List<EnderecoEntity> enderecos = enderecoRepository.findByUsuarioAndIsAtivoTrue(usuarioId);
        return enderecoMapper.toDomain(enderecos);
    }

    @Override
    public Endereco buscarPorId(Integer id) {
        EnderecoEntity enderecoExistente = enderecoRepository.findByIdAndIsAtivoTrue(id);
        if (enderecoExistente != null) {
            return enderecoMapper.toDomain(enderecoExistente);
        }
        throw new EntidadeNaoEncontradaException("Endereço com Id %d não encontrado.".formatted(id));
    }

    @Override
    public Endereco cadastrar(Endereco endereco) {
        EnderecoEntity enderecoEntity = enderecoMapper.toEntity(endereco);
        preencherDedupHash(enderecoEntity);
        if (enderecoEntity.getUsuario() != null) {
            if (existeEnderecoDuplicado(enderecoEntity) && enderecoEntity.isAtivo()) {
                throw new EntidadeJaExisteException("Endereço já cadastrado");
            }

            Usuario usuarioFk = usuarioUseCase.buscarPorId(enderecoEntity.getUsuario());

            if (!enderecoEntity.getUsuario().equals(usuarioFk.getId())) {
                throw new EntidadeNaoEncontradaException("Usuariofk não existe no banco");
            }
        }
        EnderecoEntity enderecoSalvo = enderecoRepository.save(enderecoEntity);
        return enderecoMapper.toDomain(enderecoSalvo);
    }

    @Override
    public Endereco atualizar(Integer id, Endereco endereco) {
        EnderecoEntity enderecoEntity = enderecoMapper.toEntity(endereco);
        if (!enderecoRepository.existsByIdAndIsAtivoTrue(id)) {
            throw new EntidadeNaoEncontradaException(("Endereço com Id %d não encontrado.".formatted(id)));
        }
        preencherDedupHash(enderecoEntity);
        if (existeEnderecoDuplicadoParaAtualizacao(enderecoEntity, id)) {
            throw new EntidadeJaExisteException("Endereço já cadastrado");
        }
        enderecoEntity.setId(id);
        EnderecoEntity enderecoSalvo = enderecoRepository.save(enderecoEntity);
        return enderecoMapper.toDomain(enderecoSalvo);
    }

    @Override
    public void deletar(Integer id) {
        EnderecoEntity enderecoEntity = enderecoRepository.findByIdAndIsAtivoTrue(id);
        if (enderecoEntity != null) {
            enderecoEntity.setAtivo(false);
            enderecoRepository.save(enderecoEntity);
            return;
        }
        throw new EntidadeNaoEncontradaException("Endereço com Id %d não encontrado.".formatted(id));
    }

    private Boolean existeEnderecoDuplicado(EnderecoEntity endereco) {
        if (endereco.getUsuario() == null) {
            return false;
        }
        return enderecoRepository.countByUsuarioAndDedupHashAndIsAtivoEquals(
                endereco.getUsuario(), endereco.getDedupHash(), true) > 0;
    }

    private Boolean existeEnderecoDuplicadoParaAtualizacao(EnderecoEntity endereco, Integer id) {
        if (endereco.getUsuario() == null) {
            return false;
        }
        return enderecoRepository.countByUsuarioAndDedupHashAndIsAtivoEqualsAndIdNot(
                endereco.getUsuario(), endereco.getDedupHash(), true, id) > 0;
    }

    private void preencherDedupHash(EnderecoEntity endereco) { endereco.setDedupHash(EnderecoHasher.computeDedupHash(endereco)); }
}
