package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.EnderecoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.EnderecoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository;
import com.carambolos.carambolosapi.system.security.EnderecoHasher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class EnderecoGatewayImpl implements EnderecoGateway {
    private final EnderecoRepository enderecoRepository;
    private final EnderecoMapper enderecoMapper;

    public EnderecoGatewayImpl(EnderecoRepository enderecoRepository, EnderecoMapper enderecoMapper) {
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
    }

    @Override
    public Page<Endereco> listar(Pageable pageable) {
        Page<EnderecoEntity> enderecos = enderecoRepository.findAllByIsAtivoTrue(pageable);
        return enderecoMapper.toDomain(enderecos);
    }

    @Cacheable(value = "enderecos-usuario", key = "#usuarioId")
    @Override
    public List<Endereco> listarPorUsuario(Integer usuarioId) {
        List<EnderecoEntity> enderecos = enderecoRepository.findByUsuarioAndIsAtivoTrue(usuarioId);
        return enderecoMapper.toDomain(enderecos);
    }

    @Cacheable(value = "enderecos", key = "#id")
    @Override
    public Endereco buscarPorId(Integer id) {
        EnderecoEntity enderecoExistente = enderecoRepository.findByIdAndIsAtivoTrue(id);
        if (enderecoExistente != null) {
            return enderecoMapper.toDomain(enderecoExistente);
        }
        return null;
    }

    @Override
    public Endereco cadastrar(Endereco endereco) {
        EnderecoEntity enderecoEntity = enderecoMapper.toEntity(endereco);
        preencherDedupHash(enderecoEntity);
        EnderecoEntity enderecoSalvo = enderecoRepository.save(enderecoEntity);
        return enderecoMapper.toDomain(enderecoSalvo);
    }

    @Override
    public Endereco atualizar(Integer id, Endereco endereco) {
        EnderecoEntity enderecoEntity = enderecoMapper.toEntity(endereco);
        preencherDedupHash(enderecoEntity);
        enderecoEntity.setId(id);
        EnderecoEntity enderecoSalvo = enderecoRepository.save(enderecoEntity);
        return enderecoMapper.toDomain(enderecoSalvo);
    }

    @Override
    public void deletar(Integer id) {
        EnderecoEntity enderecoEntity = enderecoRepository.findByIdAndIsAtivoTrue(id);
        enderecoEntity.setAtivo(false);
        enderecoRepository.save(enderecoEntity);
    }

    @Override
    public boolean existeEnderecoDuplicado(Endereco endereco) {
        EnderecoEntity enderecoEntity = enderecoMapper.toEntity(endereco);
        preencherDedupHash(enderecoEntity);
        return existeEnderecoDuplicado(enderecoEntity);
    }

    @Override
    public boolean existeEnderecoDuplicadoParaAtualizacao(Endereco endereco, Integer id) {
        EnderecoEntity enderecoEntity = enderecoMapper.toEntity(endereco);
        preencherDedupHash(enderecoEntity);
        return existeEnderecoDuplicadoParaAtualizacao(enderecoEntity, id);
    }

    @Override
    public boolean existsByIdAndIsAtivoTrue(Integer id) {
        return enderecoRepository.existsByIdAndIsAtivoTrue(id);
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
