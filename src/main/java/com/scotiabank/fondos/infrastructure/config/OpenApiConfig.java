package com.scotiabank.fondos.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fondosOpenApi() {
        return new OpenAPI()
                .addServersItem(new Server()
                        .url("http://localhost:8020")
                        .description("Local environment"))
                .info(new Info()
                        .title("Fondos API")
                        .version("v1")
                        .description("Documentacion OpenAPI para gestion de productos de fondos mutuos.")
                        .contact(new Contact()
                                .name("Equipo Fondos")
                                .email("fondos@scotiabank.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
