package com.mor.itas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("QA Review Management API")
                        .description("REST APIs for Quality Assurance Review Case Management")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("QA Team")
                                .email("qa@example.com")));
    }
}