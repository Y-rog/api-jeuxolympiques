package com.yrog.apijeuxolympiques.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Jeux Olympiques")
                        .description("API pour la gestion des jeux olympiques pour examen Studi")
                        .version("v1.0"));
    }
}
