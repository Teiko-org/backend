package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.repository.EnderecoRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Optional<List<Endereco>> listar() {
        return Optional.of(enderecoRepository.findAll());
    }

    public Optional<Endereco> buscarPorId(Integer id) {
        return enderecoRepository.findById(id);
    }

    public Endereco cadastrar(Endereco endereco) throws BadRequestException {
        varificarDuplicidade(endereco);
        return enderecoRepository.save(endereco);
    }

    public Endereco atualizar(Endereco endereco) throws BadRequestException {
        varificarDuplicidade(endereco);
        return enderecoRepository.save(endereco);
    }

    public Void deletar(Integer id) {
        enderecoRepository.deleteById(id);
        return null;
    }

    public boolean existePorId(Integer id) {
        return enderecoRepository.existsById(id);
    }

    public Boolean varificarDuplicidade(Endereco endereco) throws BadRequestException {
        Integer enderecoDuplicado = enderecoRepository.countByUsuarioAndCepAndNumeroEquals(endereco.getUsuario(), endereco.getCep(), endereco.getNumero());

        if (enderecoDuplicado > 0) {
            throw new BadRequestException("endereço ja cadastrado");
        }
        return false;
    }
}
