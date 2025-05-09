package cz.muni.fi.bandmanagementservice;

import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Tomáš MAREK
 */
@SpringBootApplication
public class BandServiceApplication {
    private static final String SECURITY_SCHEME_OAUTH2 = "MUNI";
    private static final String SECURITY_SCHEME_BEARER = "Bearer";
    public static final String SECURITY_SCHEME_NAME = SECURITY_SCHEME_BEARER;


    public static void main(String[] args) {
        SpringApplication.run(BandServiceApplication.class, args);
    }




}
