package com.eventpaiger.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityContextHelper {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public void setAuthenticationContext(String jwtToken){
        SecurityContext newContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = extractFromToken(jwtToken);
        newContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(newContext);
    }

    public static void clearAuthentication(){
        SecurityContextHolder.clearContext();
    }

    public static String getUsernameFromAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof UsernamePasswordAuthenticationToken token){
            return token.getName();
        } else {
            return "NO USER IN CONTEXT";
        }
    }

    private Authentication extractFromToken(String jwtToken) {
        String username = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }
}
