package org.eventpaiger.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHelper {

    public static String getUsernameFromAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof AuthenticationToken token){
            return token.getUsername();
        } else {
            return "NO USER IN CONTEXT";
        }
    }
}
