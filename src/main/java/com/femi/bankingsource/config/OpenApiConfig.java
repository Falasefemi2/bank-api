package com.femi.bankingsource.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banking API")
                        .version("1.0.0")
                        .description("A professional-grade RESTful API for banking operations, including user authentication, account management, and transaction processing. Supports role-based access control (USER, ADMIN, SUPPORT) with JWT authentication.")
                        .contact(new Contact()
                                .name("API Support Team")
                                .email("support@bankingapi.com")
                                .url("https://bankingapi.com/support"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("Local Development Server"),
                        new Server().url("https://api.bankingapi.com").description("Production Server")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token obtained from /api/auth/login or /api/auth/register. Include in the Authorization header as 'Bearer <token>'.")))
                .tags(Arrays.asList(
                        new Tag().name("Authentication").description("Endpoints for user registration and login"),
                        new Tag().name("Accounts").description("Endpoints for managing bank accounts and transactions"),
                        new Tag().name("Admin").description("Endpoints for administrative operations (ADMIN role only)")));
    }

}