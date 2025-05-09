package cz.muni.fi.bandmanagementservice.config;

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
    private static final String GENERAL_SCOPE = "SCOPE_test_1";
    private static final String MANAGER_SCOPE = "SCOPE_test_2";
    private static final String MUSICIAN_SCOPE = "SCOPE_test_3";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests( x -> x
                        // Bands API
                        .requestMatchers(HttpMethod.POST, "/api/bands").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/bands/**").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.PATCH, "/api/bands").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/bands").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/api/bands/*/members/**").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.PATCH, "/api/bands/*/members/**").hasAuthority(MANAGER_SCOPE)

                        // BandOffers API
                        .requestMatchers(HttpMethod.POST, "/api/bands/offers").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/bands/offers/**").hasAuthority(MUSICIAN_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/api/bands/offers/*/accept").hasAuthority(MUSICIAN_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/api/bands/offers/*/reject").hasAuthority(MUSICIAN_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/api/bands/offers/*/revokes").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/bands/offers").hasAnyAuthority(MANAGER_SCOPE, MUSICIAN_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/bands/offers/byBand/**").hasAnyAuthority(MANAGER_SCOPE, MUSICIAN_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/bands/offers/byMusician/**").hasAnyAuthority(MANAGER_SCOPE, MUSICIAN_SCOPE)
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()
                        )
                );
        return http.build();
    }

}
