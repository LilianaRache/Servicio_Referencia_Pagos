package com.referencedpaymentsapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI referencedPaymentsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Referenced Payments API")
                        .description("API para gestión de referencias de pago")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Jeimmy Liliana Rache Camargo")
                                .email("lilianarache@gmail.com")
                                .url("https://github.com/LilinaRache")));
    }
}
