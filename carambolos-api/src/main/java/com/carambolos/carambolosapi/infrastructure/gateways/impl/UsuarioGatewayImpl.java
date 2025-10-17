package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.UsuarioMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.UsuarioEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.UsuarioRepository;

import java.util.List;

public class UsuarioGatewayImpl implements UsuarioGateway {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioGatewayImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public List<Usuario> listar() {
        List<UsuarioEntity> usuarios = usuarioRepository.findAllByIsAtivoTrue();
        return usuarioMapper.toDomain(usuarios);
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        UsuarioEntity usuarioExistente = usuarioRepository.findByIdAndIsAtivoTrue(id);
        if (usuarioExistente != null) {
            return usuarioMapper.toDomain(usuarioExistente);
        }
        return null;
    }

    @Override
    public java.util.Optional<Usuario> findById(Integer id) {
        UsuarioEntity usuarioExistente = usuarioRepository.findByIdAndIsAtivoTrue(id);
        if (usuarioExistente != null) {
            return java.util.Optional.of(usuarioMapper.toDomain(usuarioExistente));
        }
        return java.util.Optional.empty();
    }

    @Override
    public Usuario atualizar(Integer id, Usuario usuario) {
        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuario);
        usuarioEntity.setId(id);
        UsuarioEntity entidadeSalva = usuarioRepository.save(usuarioEntity);
        return usuarioMapper.toDomain(entidadeSalva);
    }

    @Override
    public Usuario atualizarDadosPessoais(Integer id, Usuario usuario) {
        UsuarioEntity usuarioExistente = usuarioRepository.findByIdAndIsAtivoTrue(id);

        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setContato(usuario.getContato());
        usuarioExistente.setDataNascimento(usuario.getDataNascimento());
        usuarioExistente.setGenero(usuario.getGenero());

        UsuarioEntity entidadeSalva = usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toDomain(entidadeSalva);
    }

    @Override
    public Usuario cadastrar(Usuario usuario) {
        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuario);
        UsuarioEntity entidadeSalva = usuarioRepository.save(usuarioEntity);
        return usuarioMapper.toDomain(entidadeSalva);
    }

    @Override
    public void atualizarSenha(Integer id, String novaSenha) {
        UsuarioEntity usuarioEntity = usuarioRepository.findByIdAndIsAtivoTrue(id);
        usuarioEntity.setSenha(novaSenha);
        usuarioRepository.save(usuarioEntity);
    }

    @Override
    public void deletar(Integer id) {
        UsuarioEntity usuarioEntity = usuarioRepository.findByIdAndIsAtivoTrue(id);
        usuarioEntity.setAtivo(false);
        usuarioRepository.save(usuarioEntity);
    }

    @Override
    public Usuario atualizarImagemPerfil(Integer id, String imagemUrl) {
        UsuarioEntity usuarioExistente = usuarioRepository.findByIdAndIsAtivoTrue(id);
        usuarioExistente.setImagemUrl(imagemUrl);
        UsuarioEntity entidadeSalva = usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toDomain(entidadeSalva);
    }

    @Override
    public boolean existePorContatoExcluindoId(String contato, Integer id) {
        return usuarioRepository.existsByContatoAndIdNotAndIsAtivoTrue(contato, id);
    }

    @Override
    public boolean existePorContato(String contato) {
        return usuarioRepository.findByContatoAndIsAtivoTrue(contato).isPresent();
    }

    @Override
    public Usuario buscarPorContato(String contato) {
        return usuarioRepository.findByContatoAndIsAtivoTrue(contato)
                .map(usuarioMapper::toDomain)
                .orElse(null);
    }
}
