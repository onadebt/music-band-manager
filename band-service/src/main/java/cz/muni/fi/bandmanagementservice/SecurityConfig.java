package cz.muni.fi.bandmanagementservice;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Tomáš MAREK
 */
@Configuration
@Profile({ "!test"})
public class SecurityConfig {

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

}
