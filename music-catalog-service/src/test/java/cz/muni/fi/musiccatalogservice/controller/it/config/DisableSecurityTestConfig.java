package cz.muni.fi.musiccatalogservice.controller.it.config;

/**
 * @author Tomáš MAREK
 */

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@Profile("test")
public class DisableSecurityTestConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request.anyRequest().permitAll());
        http.csrf(AbstractHttpConfigurer::disable);
        http.oauth2ResourceServer(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
