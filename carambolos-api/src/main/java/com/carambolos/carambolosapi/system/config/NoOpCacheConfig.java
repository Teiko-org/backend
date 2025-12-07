package com.carambolos.carambolosapi.system.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de cache no-op (sem cache) quando Redis não está disponível.
 * Isso permite que as anotações @Cacheable e @CacheEvict funcionem sem erros,
 * mas sem realmente fazer cache.
 */
@Configuration
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "none", matchIfMissing = false)
@EnableCaching
public class NoOpCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new NoOpCacheManager();
    }
}

