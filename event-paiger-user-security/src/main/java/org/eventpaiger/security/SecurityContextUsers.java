package org.eventpaiger.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUsers {

    public static String getUsernameFromAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof AuthenticationToken token){
            return token.getUsername();
        } else {
            return "NO USER IN CONTEXT";
        }
    }

    public String getUserEmailFromAuthenticationUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof AuthenticationToken token){
            return token.getEmail();
        } else {
            return "NO USER IN CONTEXT";
        }
    }

    public TokenClaims getTokenClaimsFromToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof AuthenticationToken token){
            return token.getTokenClaims();
        } else {
            throw new ClassCastException("NO CLAIMS IN CONTEXT");
        }
    }
}
