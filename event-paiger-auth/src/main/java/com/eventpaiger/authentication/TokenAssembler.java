package com.eventpaiger.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TokenAssembler {

    public static TokenClaims mapTokenClaims(Map<String, Object> jwtClaims){
        TokenClaims tokenClaims = new TokenClaims();

        String username = (String) jwtClaims.get("sub");
        String email = (String) jwtClaims.get("email");
        UUID eventOrganizerId = UUID.fromString((String) jwtClaims.get("event_organizer_id"));
        String scope = (String) jwtClaims.get("scope");

        Collection<GrantedAuthority> authorities = Arrays.stream(scope.split(" "))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        setValuesIntoTokenClaims(tokenClaims, username, email, eventOrganizerId, scope, authorities);

        return tokenClaims;
    }

    private static void setValuesIntoTokenClaims(TokenClaims tokenClaims, String username, String email, UUID eventOrganizerId, String scope, Collection<GrantedAuthority> authorities) {
        tokenClaims.setUsername(username);
        tokenClaims.setEmail(email);
        tokenClaims.setEventOrganizerId(eventOrganizerId);
        tokenClaims.setScope(scope);
        tokenClaims.setAuthorities(authorities);
    }
}
