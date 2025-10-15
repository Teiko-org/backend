package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.UsuarioEntity;
import com.carambolos.carambolosapi.infrastructure.web.request.AtualizarUsuarioRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.request.LoginRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.request.UsuarioRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.UsuarioResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.UsuarioTokenDTO;

import java.util.List;

public class UsuarioMapper {
    public UsuarioEntity toEntity(Usuario usuarioDomain) {
        return new UsuarioEntity(
                usuarioDomain.getId(),
                usuarioDomain.getNome(),
                usuarioDomain.getContato(),
                usuarioDomain.getSenha(),
                usuarioDomain.getDataNascimento(),
                usuarioDomain.getGenero(),
                usuarioDomain.getImagemUrl(),
                usuarioDomain.isAtivo(),
                usuarioDomain.getSysAdmin()
        );
    }

    public Usuario toDomain(UsuarioEntity usuarioEntity) {
        return new Usuario(
                usuarioEntity.getId(),
                usuarioEntity.getNome(),
                usuarioEntity.getContato(),
                usuarioEntity.getSenha(),
                usuarioEntity.getDataNascimento(),
                usuarioEntity.getGenero(),
                usuarioEntity.getImagemUrl(),
                usuarioEntity.isAtivo(),
                usuarioEntity.getSysAdmin()
        );
    }

    public List<Usuario> toDomain(List<UsuarioEntity> usuariosEntity) {
        return usuariosEntity.stream().map(this::toDomain).toList();
    }

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioResponseDTO responseDto = new UsuarioResponseDTO();

        responseDto.setId(usuario.getId());
        responseDto.setNome(usuario.getNome());
        responseDto.setContato(usuario.getContato());
        responseDto.setDataNascimento(usuario.getDataNascimento());
        responseDto.setGenero(usuario.getGenero());
        responseDto.setImagemUrl(usuario.getImagemUrl());
        responseDto.setAtivo(usuario.isAtivo());
        if (usuario.getSysAdmin() == null) {
            responseDto.setAdmin(false);
        } else {
            responseDto.setAdmin(usuario.getSysAdmin());
        }

        return responseDto;
    }

    public static Usuario toDomain(UsuarioRequestDTO requestDto) {
        if(requestDto == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setNome(requestDto.getNome());
        usuario.setSenha(requestDto.getSenha());
        usuario.setContato(requestDto.getContato());
        usuario.setDataNascimento(requestDto.getDataNascimento());
        usuario.setGenero(requestDto.getGenero());
        return usuario;
    }

    public static Usuario toDomain(AtualizarUsuarioRequestDTO requestDto) {
        if(requestDto == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setNome(requestDto.getNome());
        usuario.setContato(requestDto.getContato());
        usuario.setDataNascimento(requestDto.getDataNascimento());
        usuario.setGenero(requestDto.getGenero());
        return usuario;
    }

    public static UsuarioTokenDTO toTokenDTO(UsuarioEntity usuarioEntity, String token) {
        UsuarioTokenDTO tokenDto = new UsuarioTokenDTO();

        tokenDto.setUserId(usuarioEntity.getId());
        tokenDto.setContato(usuarioEntity.getContato());
        tokenDto.setNome(usuarioEntity.getNome());
        tokenDto.setToken(token);
        tokenDto.setAdmin(usuarioEntity.sysAdmin);
        return tokenDto;
    }

    public static UsuarioTokenDTO toTokenDTO(Usuario usuario, String token) {
        UsuarioTokenDTO tokenDto = new UsuarioTokenDTO();

        tokenDto.setUserId(usuario.getId());
        tokenDto.setContato(usuario.getContato());
        tokenDto.setNome(usuario.getNome());
        tokenDto.setToken(token);
        tokenDto.setAdmin(usuario.sysAdmin);
        return tokenDto;
    }

    public static Usuario toDomain(LoginRequestDTO loginDto) {
        Usuario usuario = new Usuario();

        usuario.setContato(loginDto.getContato());
        usuario.setSenha(loginDto.getSenha());

        return usuario;
    }

}
