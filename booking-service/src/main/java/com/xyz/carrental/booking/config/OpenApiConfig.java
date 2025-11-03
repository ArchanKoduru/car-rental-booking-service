package com.xyz.carrental.booking.config;


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
                        .title("Car Rental Booking API")
                        .version("1.0.0")
                        .description("Spring Boot service to confirm and retrieve car rental bookings"));
    }
}

