package com.eventpaiger.user.helper;

import com.eventpaiger.security.AuthenticationToken;
import com.eventpaiger.security.TokenClaims;
import com.eventpaiger.user.model.user.UserProfile;
import com.eventpaiger.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityContextUsers {

    private final UserProfileRepository repository;

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

    public UserProfile getUserProfileFromAuthentication(){
        String userEmail = getUserEmailFromAuthenticationUser();
        Optional<UserProfile> userProfileByEmail = repository.findUserProfileByEmail(userEmail);

        if(userProfileByEmail.isEmpty()){
            log.error("WARNING: User with email: {} from authentication is not exists! Contact with admin", userEmail);
            throw new UsernameNotFoundException("User from authentication not found");
        }

        return userProfileByEmail.get();
    }
}
