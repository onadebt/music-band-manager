package cz.muni.fi.bandmanagementservice;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tomáš MAREK
 */
@Configuration
public class OpenAPIConfig {
    private static final String SECURITY_SCHEME_OAUTH2 = "MUNI";
    private static final String SECURITY_SCHEME_BEARER = "Bearer";
    public static final String SECURITY_SCHEME_NAME = SECURITY_SCHEME_BEARER;


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
