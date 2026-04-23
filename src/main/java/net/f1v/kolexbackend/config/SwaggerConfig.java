package net.f1v.kolexbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Kolex SprintBoard API")
                        .description("API documentation for the Kolex SprintBoard team project")
                        .version("1.0.0"));
    }
}