package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.GeocodingGateway;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class NominatimGeocodingGatewayImpl implements GeocodingGateway {

    private final RestTemplate restTemplate;

    public NominatimGeocodingGatewayImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void geocodeEndereco(Endereco endereco) {
        try {
            String query = String.format("%s, %s, %s, %s",
                    endereco.getLogradouro(),
                    endereco.getNumero(),
                    endereco.getCidade(),
                    endereco.getEstado()
            );

            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedQuery;

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "CarambolosApp/1.0 (teiko@example.com)");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<NominatimResponse[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, NominatimResponse[].class);

            if (response.getBody() != null && response.getBody().length > 0) {
                NominatimResponse firstResult = response.getBody()[0];
                endereco.setLatitude(Double.parseDouble(firstResult.getLat()));
                endereco.setLongitude(Double.parseDouble(firstResult.getLon()));
            }
        } catch (Exception e) {
            System.err.println("Erro ao geocodificar endereço: " + e.getMessage());
        }
    }

    public static class NominatimResponse {
        private String lat;
        private String lon;

        public String getLat() { return lat; }
        public void setLat(String lat) { this.lat = lat; }
        public String getLon() { return lon; }
        public void setLon(String lon) { this.lon = lon; }
    }
}
