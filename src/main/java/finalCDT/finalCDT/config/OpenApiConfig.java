package finalCDT.finalCDT.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API CDT Bancolombia")
                        .description("API para gestión de CDTs y servicios bancarios")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("desarrollo@bancolombia.com.co")));
    }
}
