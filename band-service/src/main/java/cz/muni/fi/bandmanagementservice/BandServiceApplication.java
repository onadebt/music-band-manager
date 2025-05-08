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

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests( x -> x
                        // Bands API
                        .requestMatchers(HttpMethod.POST, "/api/bands").hasAuthority("SCOPE_test_2")
                        .requestMatchers(HttpMethod.GET, "/api/bands/**").hasAuthority("SCOPE_test_1")
                        .requestMatchers(HttpMethod.PATCH, "/api/bands").hasAuthority("SCOPE_test_2")
                        .requestMatchers(HttpMethod.GET, "/api/bands").hasAuthority("SCOPE_test_1")
                        .requestMatchers(HttpMethod.DELETE, "/api/bands/**/members/**").hasAuthority("SCOPE_test_2")
                        .requestMatchers(HttpMethod.PATCH, "/api/bands/**/members/**").hasAuthority("SCOPE_test_2")

                        // BandOffers API
                        .requestMatchers(HttpMethod.POST, "/api/bands/offers").hasAuthority("SCOPE_test_2")
                        .requestMatchers(HttpMethod.GET, "/api/bands/offers/**").hasAuthority("SCOPE_test_3")
                        .requestMatchers(HttpMethod.POST, "/api/bands/offers/**/accept").hasAuthority("SCOPE_test_3")
                        .requestMatchers(HttpMethod.POST, "/api/bands/offers/**/reject").hasAuthority("SCOPE_test_3")
                        .requestMatchers(HttpMethod.POST, "/api/bands/offers/**/revokes").hasAuthority("SCOPE_test_2")
                        .requestMatchers(HttpMethod.GET, "/api/bands/offers").hasAnyAuthority("SCOPE_test_2", "SCOPE_test_3")
                        .requestMatchers(HttpMethod.GET, "/api/bands/offers/byBand/**").hasAnyAuthority("SCOPE_test_2", "SCOPE_test_3")
                        .requestMatchers(HttpMethod.GET, "/api/bands/offers/byMusician/**").hasAnyAuthority("SCOPE_test_2", "SCOPE_test_3")
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()
                )
            );
        return http.build();
    }

    @Bean
    public OpenApiCustomizer openAPICustomizer() {
        return openApi -> {
            openApi.getComponents()
//                    .addSecuritySchemes(SECURITY_SCHEME_OAUTH2,
//                            new SecurityScheme()
//                                    .type(SecurityScheme.Type.OAUTH2)
//                                    .description("get access token with OAuth 2 Authorization Code Grant")
//                                    .flows(new OAuthFlows()
//                                            .authorizationCode(new OAuthFlow()
//                                                    .authorizationUrl("https://id.muni.cz/oidc/authorize")
//                                                    .tokenUrl("https://id.muni.cz/oidc/token")
//                                                    .scopes(new Scopes()
//                                                            .addString("openid", "get user identifier")
//                                                            .addString("test_1", "general operations")
//                                                            .addString("test_2", "manager operations")
//                                                            .addString("test_3", "musician operations")
//                                                    )
//                                            )
//                                    )
//                    )
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
