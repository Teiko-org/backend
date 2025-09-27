package com.carambolos.carambolosapi.system.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Simlady Server",
                description = "Aplicação desenvolvida com Java Spring, focada no gerenciamento de produtos, clientes e vendas para otimizar a operação de uma plataforma de e-commerce. A API permite o cadastro, atualização e consulta de dados relacionados aos produtos e clientes, além de gerenciar as vendas realizadas.",
                version = "1.0.0"
        )
)
@SecurityScheme(
        name = "Bearer",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
