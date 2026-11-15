package com.vaibhavsharmaggwp.json_query_engine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JSON Dataset Query Engine API")
                        .version("1.0.0")
                        .description("A high-performance Spring Boot service for dynamic persistence, grouping, and sorting of arbitrary JSON datasets over PostgreSQL.")
                        .contact(new Contact()
                                .name("Vaibhav Sharma")
                                .url("https://github.com/VaibhavSharmaggwp/json-dataset-query-engine")));
    }
}
