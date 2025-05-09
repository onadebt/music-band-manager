package cz.muni.fi.userservice.config;

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
                        // Artist API
                        .requestMatchers(HttpMethod.GET, "/api/artists/**").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/api/artists/**").hasAuthority(MUSICIAN_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/api/artists/**").hasAuthority(MUSICIAN_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/artists").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/api/artists").hasAuthority(MUSICIAN_SCOPE)
                        .requestMatchers(HttpMethod.PATCH, "/api/artists/unlink/{artistId}/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.PATCH, "/api/artists/link/{artistId}/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.PATCH, "/api/artists/bands/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/artists/username/**").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/artists/bands").hasAuthority(GENERAL_SCOPE)

                        // Manager API
                        .requestMatchers(HttpMethod.GET, "/api/managers/**").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/api/managers/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/api/managers/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/managers").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/api/managers").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.PATCH, "/api/managers/bands/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/managers/bands").hasAuthority(GENERAL_SCOPE)

                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()
                        )
                );
        return http.build();
    }
}

