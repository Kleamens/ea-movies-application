package cz.mendelu.ea.xzirchuk.project.moviesAPI.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIConfig {


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()


                .info(new Info()
                        .title("Movies")
                        .description("Movies API")
                        .version("1.0")
                );
    }



}
