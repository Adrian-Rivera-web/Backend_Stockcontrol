package com.appstockcontrol.inventario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI movimientosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Movimientos de Inventario - AppStockControl")
                        .description("Microservicio de registro de entradas y salidas de stock (Kardex)")
                        .version("1.0.0"));
    }
}
