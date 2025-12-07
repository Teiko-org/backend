package com.carambolos.carambolosapi;

import com.carambolos.carambolosapi.system.security.EnderecoHasher;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import io.github.cdimascio.dotenv.Dotenv;

@OpenAPIDefinition(
        info = @Info(
                title = "API Carambolos",
                version = "v1",
                description = "API para gerenciamento de pedidos e usuários da Confeitaria Carambolos"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor Local")
        }
)
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class
})
public class CarambolosApiApplication {

    public static void main(String[] args) {
        // Em desenvolvimento local, usamos dev.env para preencher variáveis.
        // Em produção (Docker/EC2), confiamos nas variáveis de ambiente já injetadas.
        try {
            Dotenv dotenv = Dotenv.configure()
                    .filename("dev.env")
                    .ignoreIfMissing()
                    .load();

            setIfPresent("spring.datasource.username", dotenv, "DB_USERNAME");
            setIfPresent("spring.datasource.password", dotenv, "DB_PASSWORD");
            setIfPresent("spring.datasource.url", dotenv, "DB_URL");

            setIfPresent("jwt.validity", dotenv, "JWT_VALIDITY");
            setIfPresent("jwt.secret", dotenv, "JWT_SECRET");

            // AWS S3
            setIfPresent("aws.s3.bucket-name", dotenv, "AWS_S3_BUCKET_NAME");
            setIfPresent("aws.region", dotenv, "AWS_REGION");

            // Redis (opcional)
            setIfPresent("REDIS_HOST", dotenv, "REDIS_HOST");
            setIfPresent("REDIS_PORT", dotenv, "REDIS_PORT");
        } catch (Exception ignored) {
            // Se dev.env não estiver presente ou der erro, seguimos só com env vars normais.
        }

        SpringApplication.run(CarambolosApiApplication.class, args);
    }

    private static void setIfPresent(String sysPropKey, Dotenv dotenv, String envKey) {
        if (dotenv == null) return;
        String value = dotenv.get(envKey);
        if (value != null && !value.isBlank()) {
            System.setProperty(sysPropKey, value);
        }
    }

    @Bean
    @ConditionalOnBean(EnderecoRepository.class)
    @SuppressWarnings("unused")
    CommandLineRunner backfillEnderecoDedupHash(EnderecoRepository enderecoRepository) {
        return args -> enderecoRepository.findAll().stream()
                .filter(e -> e.getDedupHash() == null || e.getDedupHash().isBlank())
                .forEach(e -> {
                    e.setDedupHash(EnderecoHasher.computeDedupHash(e));
                    enderecoRepository.save(e);
                });
    }
}