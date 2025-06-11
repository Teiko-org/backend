package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    Integer countByUsuarioAndCepAndNumeroAndIsAtivoEquals(Integer usuario, String cep, String numero, boolean isAtivo);
    Integer countByUsuarioAndCepAndNumeroAndIsAtivoEqualsAndIdNot(Integer usuario, String cep, String numero, boolean isAtivo, Integer id);
    List<Endereco> findAllByIsAtivoTrue();
    List<Endereco> findByUsuarioAndIsAtivoTrue(Integer usuario);
    Endereco findByIdAndIsAtivoTrue(Integer id);
    boolean existsByIdAndIsAtivoTrue(Integer id);

}
