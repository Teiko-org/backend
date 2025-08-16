package com.carambolos.carambolosapi.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class HashUtils {

    private HashUtils() {}

    private static SecretKeySpec loadPepper() {
        String base64Key = System.getenv("CRYPTO_SECRET_B64");
        if (base64Key == null || base64Key.isBlank()) {
            base64Key = System.getProperty("CRYPTO_SECRET_B64");
        }
        if (base64Key == null || base64Key.isBlank()) {
            throw new IllegalStateException("CRYPTO_SECRET_B64 não definido para HMAC.");
        }
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public static String hmacSha256Base64(String input) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(loadPepper());
            byte[] h = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(h);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao calcular HMAC-SHA256", e);
        }
    }
}


