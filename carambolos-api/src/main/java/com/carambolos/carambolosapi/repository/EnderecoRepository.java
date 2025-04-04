package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    Integer countByUsuarioAndCepAndNumeroEquals(Integer usuario, String cep, String numero);
}
