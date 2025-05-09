package cz.muni.fi.musiccatalogservice.config;

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
                        // Album API
                        .requestMatchers(HttpMethod.GET, "/api/albums/**").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/api/albums/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/api/albums/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/albums").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/api/albums").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/albums/band/**").hasAuthority(GENERAL_SCOPE)

                        // Song API
                        .requestMatchers(HttpMethod.GET, "/api/songs/**").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/api/songs/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/api/songs/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/songs").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/api/songs").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/songs/band/**").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/songs/album/**").hasAuthority(GENERAL_SCOPE)

                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()
                        )
                );
        return http.build();
    }

}
