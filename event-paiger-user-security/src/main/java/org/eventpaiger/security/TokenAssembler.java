package org.eventpaiger.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

class TokenAssembler {

    static TokenClaims mapTokenClaims(Map<String, Object> jwtClaims){

        String username = (String) jwtClaims.get("sub");
        String email = (String) jwtClaims.get("email");

        String eventOrganizerId = (String) jwtClaims.get("event_organizer_id");
        String scope = (String) jwtClaims.get("scope");

        Collection<GrantedAuthority> authorities = Arrays.stream(scope.split(" "))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return buildTokenClaims(username, email, eventOrganizerId, authorities);
    }

    private static TokenClaims buildTokenClaims(String username,
                                                String email,
                                                String eventOrganizerId,
                                                Collection<GrantedAuthority> authorities) {
        return TokenClaims.builder()
                .username(username)
                .email(email)
                .eventOrganizerId(eventOrganizerId)
                .authorities(authorities)
                .build();
    }
}
