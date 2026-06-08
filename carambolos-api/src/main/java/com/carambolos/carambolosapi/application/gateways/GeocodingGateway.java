package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.Endereco;

public interface GeocodingGateway {
    void geocodeEndereco(Endereco endereco);
}
