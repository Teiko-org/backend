package com.carambolos.carambolosapi.system.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Conversor "no-op" para este ambiente.
 *
 * Os dados existentes no banco foram gravados com chaves antigas e inconsistentes,
 * então não conseguimos mais descriptografar de forma confiável. Para não quebrar
 * o fluxo da aplicação e nem exibir textos cifrados para o usuário, este conversor
 * passa a gravar e ler os valores em texto puro.
 *
 * IMPORTANTE: este é um projeto acadêmico/lab. Em produção real, o ideal seria
 * uma migração de dados com rotação de chave, não um no-op.
 */
@Converter
public class CryptoAttributeConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        // Não criptografa mais; grava o valor em texto puro.
        return attribute;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        // Retorna o valor exatamente como está no banco.
        return dbData;
    }
}

