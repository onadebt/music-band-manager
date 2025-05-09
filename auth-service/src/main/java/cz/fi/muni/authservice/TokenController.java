package cz.fi.muni.authservice;

import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.Map;
import java.util.Objects;

@RestController
public class TokenController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public TokenController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/token")
    public Map<String, String> getToken(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        return Map.of(
                "accessToken", client.getAccessToken().getTokenValue(),
                "expiresAt", Objects.requireNonNull(client.getAccessToken().getExpiresAt()).atZone(ZoneId.systemDefault()).toString(),
                "scopes", client.getAccessToken().getScopes().toString()
        );
    }
}