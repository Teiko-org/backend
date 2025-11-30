package com.carambolos.carambolosapi;

import com.carambolos.carambolosapi.system.security.EnderecoHasher;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import io.github.cdimascio.dotenv.Dotenv;

@OpenAPIDefinition(
		info = @Info(
				title = "API Carambolos",
				version = "v1",
				description = "API para gerenciamento de pedidos e usuários da Confeitaria Carambolos"
		), servers = {
@Server(url = "http://localhost:8080", description = "Servidor Local")
    }
)
@SpringBootApplication
@EnableCaching
public class CarambolosApiApplication {

	public static void main(String[] args) {
		// Em desenvolvimento local, usamos dev.env para preencher variáveis.
		// Em produção (Docker/EC2), confiamos nas variáveis de ambiente já injetadas
		// e não forçamos sobrescrita com valores nulos.
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

			setIfPresent("azure.storage.connection-string", dotenv, "AZURE_STORAGE_CONNECTION_STRING");
			setIfPresent("azure.storage.container-name", dotenv, "AZURE_STORAGE_CONTAINER_NAME");

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
