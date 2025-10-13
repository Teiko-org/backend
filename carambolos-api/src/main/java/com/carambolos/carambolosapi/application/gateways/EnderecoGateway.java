package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.Endereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EnderecoGateway {
    Page<Endereco> listar(Pageable pageable);
    List<Endereco> listarPorUsuario(Integer usuarioId);
    Endereco buscarPorId(Integer id);
    Endereco cadastrar(Endereco endereco);
    Endereco atualizar(Integer id, Endereco endereco);
    void deletar(Integer id);
}
