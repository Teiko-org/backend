package com.carambolos.carambolosapi.system.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Configuração do Redis para cache APENAS de endereços.
 * Esta configuração só é carregada quando spring.cache.type=redis.
 * 
 * IMPORTANTE: Apenas EnderecoUseCase deve usar anotações @Cacheable/@CacheEvict.
 */
@Configuration
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis", matchIfMissing = false)
@EnableCaching
public class RedisConfig implements CachingConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        try {
            // Testa a conexão antes de criar o CacheManager
            redisConnectionFactory.getConnection().ping();
            LOGGER.info("✅ Redis conectado - Cache de endereços ativado");
            
            return RedisCacheManager.builder(redisConnectionFactory)
                    .cacheDefaults(cacheConfiguration())
                    .transactionAware()
                    .build();
        } catch (Exception e) {
            LOGGER.error("❌ Erro ao conectar ao Redis: {}", e.getMessage());
            LOGGER.warn("⚠️ Aplicação continuará sem cache de endereços");
            throw new RuntimeException("Falha ao configurar Redis CacheManager", e);
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                LOGGER.warn("Erro ao buscar do cache {} com chave {}: {}. Limpando entrada do cache.", cache != null ? cache.getName() : "unknown", key, exception.getMessage());
                try {
                    if (cache != null) {
                        cache.evict(key);
                    }
                } catch (Exception e) {
                    LOGGER.warn("Erro ao limpar cache após erro de deserialização: {}", e.getMessage());
                }
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                LOGGER.warn("Erro ao salvar no cache {} com chave {}: {}", cache != null ? cache.getName() : "unknown", key, exception.getMessage());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                LOGGER.warn("Erro ao limpar cache {} com chave {}: {}", cache != null ? cache.getName() : "unknown", key, exception.getMessage());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                LOGGER.warn("Erro ao limpar cache {}: {}", cache != null ? cache.getName() : "unknown", exception.getMessage());
            }
        };
    }
}
