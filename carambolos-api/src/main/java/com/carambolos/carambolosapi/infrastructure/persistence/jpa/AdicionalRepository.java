package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.AdicionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdicionalRepository extends JpaRepository<AdicionalEntity, Integer> {
}
