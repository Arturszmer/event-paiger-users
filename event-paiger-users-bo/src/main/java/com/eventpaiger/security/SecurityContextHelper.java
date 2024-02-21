package com.eventpaiger.security;

import com.eventpaiger.user.model.UserProfile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Component
public class SecurityContextHelper {

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

    public static UserProfile getUserFromAuthentication() throws UserPrincipalNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof UsernamePasswordAuthenticationToken user){
            return (UserProfile) user.getPrincipal();
        } else {
            throw new UserPrincipalNotFoundException("User is not found");
        }
    }
}
