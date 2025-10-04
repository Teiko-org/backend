package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.domain.entity.Endereco;

import java.util.List;

public class EnderecoUseCase {
    private final EnderecoGateway enderecoGateway;

    public EnderecoUseCase(EnderecoGateway enderecoGateway) {
        this.enderecoGateway = enderecoGateway;
    }

    public List<Endereco> listar() {
        return enderecoGateway.listar();
    }

    public List<Endereco> listarPorUsuario(Integer usuarioId) {
        return enderecoGateway.listarPorUsuario(usuarioId);
    }

    public Endereco buscarPorId(Integer id) {
        return enderecoGateway.buscarPorId(id);
    }

    public Endereco cadastrar(Endereco endereco) {
        return enderecoGateway.cadastrar(endereco);
    }

    public Endereco atualizar(Integer id, Endereco endereco) {
        return enderecoGateway.atualizar(id, endereco);
    }

    public void deletar(Integer id) {
        enderecoGateway.deletar(id);
    }
}
