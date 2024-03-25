package com.eventpaiger.security;

import lombok.Getter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;

@Getter
public class AuthenticationToken extends JwtAuthenticationToken {

    private final String username;
    private final String email;
    private final String eventOrganizerId;
    private final String scope;

    AuthenticationToken(Jwt jwt, TokenClaims tokenClaims){
        super(jwt, tokenClaims.getAuthorities());
        this.username = tokenClaims.getUsername();
        this.email = tokenClaims.getEmail();
        this.eventOrganizerId = tokenClaims.getEventOrganizerId();
        this.scope = tokenClaims.getScope();
    }

    public Instant getExpirationDate(){
        return super.getToken().getExpiresAt();
    }

    public TokenClaims getTokenClaims(){
        return TokenClaims.builder()
                .authorities(getAuthorities())
                .email(this.email)
                .eventOrganizerId(this.eventOrganizerId)
                .username(this.username)
                .scope(this.scope)
                .build();
    }
}
