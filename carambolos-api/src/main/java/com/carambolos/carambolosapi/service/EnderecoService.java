package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Endereco cadastrar(Endereco endereco) {
        try {
            Optional<Endereco> enderecoDuplicado = enderecoRepository.findByUsuarioAndCepAndNumeroEquals(endereco.getUsuario(), endereco.getCep(), endereco.getNumero());

            if (enderecoDuplicado.isPresent()) {

            }


            return new Endereco();
        } catch (Exception e) {
            throw e;
        }
    }
}
