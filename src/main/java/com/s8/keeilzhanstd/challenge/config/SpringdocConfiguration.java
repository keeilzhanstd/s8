package com.s8.keeilzhanstd.challenge.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SpringdocConfiguration {

    @Bean
    public OpenAPI baseOpenApi() {
        return new OpenAPI().info(new Info().title("S8 Challenge").version("1.0.0").description("S8 backend challenge docs"));
    }
}
