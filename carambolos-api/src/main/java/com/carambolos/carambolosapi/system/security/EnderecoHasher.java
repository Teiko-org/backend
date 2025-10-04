package com.carambolos.carambolosapi.system.security;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.EnderecoEntity;

import static java.util.Objects.requireNonNullElse;

public final class EnderecoHasher {
    private EnderecoHasher() {}

    public static String computeDedupHash(EnderecoEntity e) {
        String base = String.join("|",
                safe(e.getCep()),
                safeLower(e.getLogradouro()),
                safeLower(e.getNumero()),
                safeLower(e.getBairro()),
                safeLower(e.getCidade()),
                safeUpper(e.getEstado())
        );
        return HashUtils.hmacSha256Base64(base);
    }

    private static String safe(String v) { return requireNonNullElse(v, "").trim(); }
    private static String safeLower(String v) { return safe(v).toLowerCase(); }
    private static String safeUpper(String v) { return safe(v).toUpperCase(); }
}


