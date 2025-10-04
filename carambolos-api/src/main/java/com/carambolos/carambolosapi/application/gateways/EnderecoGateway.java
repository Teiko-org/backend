package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.Endereco;

import java.util.List;

public interface EnderecoGateway {
    List<Endereco> listar();
    List<Endereco> listarPorUsuario(Integer usuarioId);
    Endereco buscarPorId(Integer id);
    Endereco cadastrar(Endereco endereco);
    Endereco atualizar(Integer id, Endereco endereco);
    void deletar(Integer id);
}
