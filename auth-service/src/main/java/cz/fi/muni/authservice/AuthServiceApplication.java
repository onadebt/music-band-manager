package cz.fi.muni.authservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@SpringBootApplication
public class AuthServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }




    @Bean
    public SecurityFilterChain securityFilterChain(ClientRegistrationRepository clientRegistrationRepository, HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(
                        x -> x
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(
                        x -> x
                                .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
                )
                .logout(
                    x->x.logoutSuccessUrl("/")
                            .logoutSuccessHandler(new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository))
                )
                .csrf(
                        c -> c
                                //set CSRF token cookie "XSRF-TOKEN" with httpOnly=false that can be read by JavaScript
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                //replace the default XorCsrfTokenRequestAttributeHandler with one that can use value from the cookie
                                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                );
        return httpSecurity.build();
    }




}
