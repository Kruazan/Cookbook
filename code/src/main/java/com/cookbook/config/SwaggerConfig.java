package com.cookbook.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI medCabinetOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CookBook API")
                        .description("API для рецептиков чисто")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Kruazan")
                                .email("support@noname.com")));
    }
}