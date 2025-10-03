package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.EnderecoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<EnderecoEntity, Integer> {
    Integer countByUsuarioAndDedupHashAndIsAtivoEquals(Integer usuario, String dedupHash, boolean isAtivo);
    Integer countByUsuarioAndDedupHashAndIsAtivoEqualsAndIdNot(Integer usuario, String dedupHash, boolean isAtivo, Integer id);
    List<EnderecoEntity> findAllByIsAtivoTrue();
    List<EnderecoEntity> findByUsuarioAndIsAtivoTrue(Integer usuario);
    EnderecoEntity findByIdAndIsAtivoTrue(Integer id);
    boolean existsByIdAndIsAtivoTrue(Integer id);

}
