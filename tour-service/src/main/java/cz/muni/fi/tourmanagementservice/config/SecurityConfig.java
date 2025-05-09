package cz.muni.fi.tourmanagementservice.config;


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
                        // Tour API
                        .requestMatchers(HttpMethod.GET, "/api/tours/**").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/api/tours/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/api/tours/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/tours").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/api/tours").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/api/tours/{tourId}/city-visit").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/tours/band/**").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/api/tours/{tourId}/city-visit/**").hasAuthority(MANAGER_SCOPE)

                        // CityVisit API
                        .requestMatchers(HttpMethod.GET, "/api/cityVisits/**").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/api/cityVisits/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/api/cityVisits/**").hasAuthority(MANAGER_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/api/cityVisits").hasAuthority(GENERAL_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/api/cityVisits").hasAuthority(MANAGER_SCOPE)

                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()
                        )
                );
        return http.build();
    }
}


