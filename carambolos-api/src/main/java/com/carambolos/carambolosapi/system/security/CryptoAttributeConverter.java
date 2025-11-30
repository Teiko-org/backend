package com.carambolos.carambolosapi.system.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Converter
public class CryptoAttributeConverter implements AttributeConverter<String, String> {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH_BITS = 128;
    private static final int IV_LENGTH_BYTES = 12;

    private static final SecretKeySpec SECRET_KEY = loadKeyFromEnv();

    private static SecretKeySpec loadKeyFromEnv() {
        String base64Key = System.getenv("CRYPTO_SECRET_B64");
        if (base64Key == null || base64Key.isBlank()) {
            base64Key = System.getProperty("CRYPTO_SECRET_B64");
        }
        if (base64Key == null || base64Key.isBlank()) {
            throw new IllegalStateException("CRYPTO_SECRET_B64 não definido. Forneça uma chave AES-256 em Base64 via variável de ambiente ou propriedade de sistema.");
        }

        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        if (keyBytes.length != 32) {
            throw new IllegalStateException("A chave fornecida em CRYPTO_SECRET_B64 deve ter 32 bytes (256 bits) após decodificar Base64.");
        }
        return new SecretKeySpec(keyBytes, "AES");
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return attribute;
        }
        try {
            byte[] iv = new byte[IV_LENGTH_BYTES];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, gcmSpec);
            byte[] cipherText = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));

            byte[] output = ByteBuffer.allocate(iv.length + cipherText.length)
                    .put(iv)
                    .put(cipherText)
                    .array();

            return Base64.getEncoder().encodeToString(output);
        } catch (Exception e) {
            // Fallback: em caso de falha de criptografia, retorna o valor original
            // para não quebrar fluxos de leitura/gravação em produção.
            System.err.println("Falha ao criptografar atributo, retornando valor original: " + e.getMessage());
            return attribute;
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return dbData;
        }
        try {
            byte[] all;
            try {
                all = Base64.getDecoder().decode(dbData);
            } catch (IllegalArgumentException notBase64) {
                return dbData;
            }
            if (all.length <= IV_LENGTH_BYTES) {
                return dbData;
            }
            byte[] iv = Arrays.copyOfRange(all, 0, IV_LENGTH_BYTES);
            byte[] cipherText = Arrays.copyOfRange(all, IV_LENGTH_BYTES, all.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, gcmSpec);
            byte[] plain = cipher.doFinal(cipherText);
            return new String(plain, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Fallback: se não conseguir descriptografar (chave trocada, dado antigo, etc.),
            // devolve o valor cru do banco para não causar 500 na aplicação.
            System.err.println("Falha ao descriptografar atributo, retornando valor cru: " + e.getMessage());
            return dbData;
        }
    }
}


