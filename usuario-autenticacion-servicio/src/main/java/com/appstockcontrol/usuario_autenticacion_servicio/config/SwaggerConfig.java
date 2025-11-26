package com.appstockcontrol.usuario_autenticacion_servicio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI usuariosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Usuarios & Auth - AppStockControl")
                        .description("Microservicio de gestión de usuarios y autenticación")
                        .version("1.0.0"));
    }
}
