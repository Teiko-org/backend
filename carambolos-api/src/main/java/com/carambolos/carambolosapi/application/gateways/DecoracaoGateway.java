package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.Decoracao;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DecoracaoGateway {
    Decoracao save(Decoracao decoracao);
    List<Decoracao> findByIsAtivoTrue();
    List<Decoracao> findByIsAtivoTrueAndCategoriaIsNotNull();
    Decoracao findById(Integer id);
}
