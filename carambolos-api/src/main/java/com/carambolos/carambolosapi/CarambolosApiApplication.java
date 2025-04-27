package com.carambolos.carambolosapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
		SpringApplication.run(CarambolosApiApplication.class, args);
	}
}
