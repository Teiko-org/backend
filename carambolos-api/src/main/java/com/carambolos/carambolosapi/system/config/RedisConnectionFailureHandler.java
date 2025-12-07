package com.carambolos.carambolosapi.system.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Handler que verifica se o Redis está disponível antes da aplicação iniciar.
 * Se não estiver disponível, desabilita o cache automaticamente.
 */
public class RedisConnectionFailureHandler implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConnectionFailureHandler.class);
    private static final int CONNECTION_TIMEOUT_MS = 10000; // 10 segundos
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_MS = 2000; // 2 segundos entre tentativas

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        
        // Verifica se o cache está configurado para Redis
        String cacheType = environment.getProperty("spring.cache.type", "none");
        if (!"redis".equals(cacheType)) {
            return;
        }

        // Verifica se CACHE_TYPE foi explicitamente definido
        String explicitCacheType = environment.getProperty("CACHE_TYPE");
        if (explicitCacheType != null && !explicitCacheType.isBlank()) {
            // Se foi definido explicitamente, respeita a configuração
            LOGGER.info("📋 Cache type definido explicitamente: {}", explicitCacheType);
            return;
        }

        // Tenta verificar se o Redis está disponível
        String redisHost = environment.getProperty("spring.data.redis.host", "redis");
        String redisPortStr = environment.getProperty("spring.data.redis.port", "6379");
        
        int redisPort;
        try {
            redisPort = Integer.parseInt(redisPortStr);
        } catch (NumberFormatException e) {
            LOGGER.warn("⚠️ Porta do Redis inválida: {}. Desabilitando cache.", redisPortStr);
            environment.getSystemProperties().put("spring.cache.type", "none");
            return;
        }

        if (!isRedisAvailableWithRetry(redisHost, redisPort)) {
            LOGGER.warn("⚠️ Redis não está disponível em {}:{} após {} tentativas. Desabilitando cache automaticamente.", 
                    redisHost, redisPort, MAX_RETRIES);
            LOGGER.warn("💡 A aplicação funcionará normalmente, mas sem cache de endereços.");
            LOGGER.warn("💡 Para ativar o cache, certifique-se de que o Redis está rodando e acessível.");
            environment.getSystemProperties().put("spring.cache.type", "none");
        } else {
            LOGGER.info("✅ Redis detectado em {}:{}. Cache de endereços será ativado.", redisHost, redisPort);
        }
    }

    private boolean isRedisAvailableWithRetry(String host, int port) {
        for (int i = 0; i < MAX_RETRIES; i++) {
            if (isRedisAvailable(host, port)) {
                return true;
            }
            if (i < MAX_RETRIES - 1) {
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }

    private boolean isRedisAvailable(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), CONNECTION_TIMEOUT_MS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
