package com.carambolos.carambolosapi.system.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@ConditionalOnBean(RedisConnectionFactory.class)
public class RedisHealthCheckConfig implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisHealthCheckConfig.class);
    private final Environment environment;
    private final RedisConnectionFactory redisConnectionFactory;

    public RedisHealthCheckConfig(Environment environment, RedisConnectionFactory redisConnectionFactory) {
        this.environment = environment;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Verifica se o cache está configurado para Redis
        String cacheType = environment.getProperty("spring.cache.type", "none");
        if (!"redis".equals(cacheType)) {
            return;
        }

        // Tenta conectar ao Redis
        try {
            redisConnectionFactory.getConnection().ping();
            LOGGER.info("✅ Redis conectado com sucesso - Cache de endereços ativo");
        } catch (Exception e) {
            LOGGER.warn("⚠️ Redis não está disponível - Aplicação funcionará sem cache. Erro: {}", e.getMessage());
            LOGGER.warn("💡 Para ativar o cache, certifique-se de que o Redis está rodando e acessível");
        }
    }
}
