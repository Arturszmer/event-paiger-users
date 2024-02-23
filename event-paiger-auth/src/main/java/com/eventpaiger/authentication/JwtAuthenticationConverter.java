package com.eventpaiger.authentication;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;

@AllArgsConstructor
public class JwtAuthenticationConverter {

    public static AuthenticationToken convert(Jwt jwt){

        TokenClaims tokenClaims = TokenAssembler.mapTokenClaims(jwt.getClaims());

        return new AuthenticationToken(jwt, tokenClaims);
    }
}
