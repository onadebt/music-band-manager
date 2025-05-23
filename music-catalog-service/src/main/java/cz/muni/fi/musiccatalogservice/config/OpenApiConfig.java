package cz.muni.fi.musiccatalogservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String SECURITY_SCHEME_BEARER = "Bearer";
    public static final String SECURITY_SCHEME_NAME = SECURITY_SCHEME_BEARER;


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Music Catalog API")
                        .version("1.0")
                        .description("Spring Boot REST API for Music Catalog")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                        .contact(new Contact()
                                .name("Music Catalog Team")
                                .url("https://github.com/music-catalog")
                                .email("info@musiccatalog.com")));
    }

    @Bean
    public OpenApiCustomizer openAPICustomizer() {
        return openApi -> {
            openApi.getComponents()
                    .addSecuritySchemes(SECURITY_SCHEME_BEARER,
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .description("provide a valid access token")
                    )
            ;
        };
    }
}