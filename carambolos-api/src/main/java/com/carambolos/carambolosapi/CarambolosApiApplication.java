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
public class CarambolosApiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.filename("dev.env")
				.load();

		System.setProperty("spring.datasource.username", dotenv.get("DB_USERNAME"));
		System.setProperty("spring.datasource.password", dotenv.get("DB_PASSWORD"));
		System.setProperty("spring.datasource.url", dotenv.get("DB_URL"));

		System.setProperty("jwt.validity", dotenv.get("JWT_VALIDITY"));
		System.setProperty("jwt.secret", dotenv.get("JWT_SECRET"));

		System.setProperty("azure.storage.connection-string", dotenv.get("AZURE_STORAGE_CONNECTION_STRING"));
		System.setProperty("azure.storage.container-name", dotenv.get("AZURE_STORAGE_CONTAINER_NAME"));

		SpringApplication.run(CarambolosApiApplication.class, args);
	}

	@Bean
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
