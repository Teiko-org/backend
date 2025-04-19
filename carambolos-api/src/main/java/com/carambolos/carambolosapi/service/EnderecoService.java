package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    public List<Endereco> listar() {
        return enderecoRepository.findAll();
    }

    public Endereco buscarPorId(Integer id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço com Id %d não encontrado.".formatted(id)));
    }

    public Endereco cadastrar(Endereco endereco) {
        if (existeEnderecoDuplicado(endereco)) {
            throw new EntidadeJaExisteException("Endereço já cadastrado");
        }
        return enderecoRepository.save(endereco);
    }

    public Endereco atualizar(Integer id, Endereco endereco) {
        if (!enderecoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException(("Endereço com Id %d não encontrado.".formatted(id)));
        }
        if (existeEnderecoDuplicado(endereco)) {
            throw new EntidadeJaExisteException("Endereço já cadastrado");
        }
        endereco.setId(id);
        return enderecoRepository.save(endereco);
    }

    public void deletar(Integer id) {
        if (!enderecoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Endereço com Id %d não encontrado.".formatted(id));
        }
        enderecoRepository.deleteById(id);
    }

    public Boolean existeEnderecoDuplicado(Endereco endereco) {
        return enderecoRepository.countByUsuarioAndCepAndNumeroEquals(
                endereco.getUsuario(), endereco.getCep(), endereco.getNumero()) > 0;
    }
}
