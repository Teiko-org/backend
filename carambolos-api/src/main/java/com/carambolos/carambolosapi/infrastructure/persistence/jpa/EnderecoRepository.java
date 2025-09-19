package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.domain.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    Integer countByUsuarioAndDedupHashAndIsAtivoEquals(Integer usuario, String dedupHash, boolean isAtivo);
    Integer countByUsuarioAndDedupHashAndIsAtivoEqualsAndIdNot(Integer usuario, String dedupHash, boolean isAtivo, Integer id);
    List<Endereco> findAllByIsAtivoTrue();
    List<Endereco> findByUsuarioAndIsAtivoTrue(Integer usuario);
    Endereco findByIdAndIsAtivoTrue(Integer id);
    boolean existsByIdAndIsAtivoTrue(Integer id);

}
