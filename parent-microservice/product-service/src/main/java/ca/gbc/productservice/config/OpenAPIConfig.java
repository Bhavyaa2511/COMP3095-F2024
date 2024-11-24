package ca.gbc.productservice.config;


import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import  org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Value("${product-service.version}")
    private String version;


    @Bean
    public OpenAPI productServiceAPI() {

        return new OpenAPI()
                .info(new Info().title("Product Service API")
                        .description("this is the rest API for product-service ")
                        .version(version)
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("product service wiki documentation")
                        .url(("https://mycompany.ca/product-service/docs")));
    }
}
