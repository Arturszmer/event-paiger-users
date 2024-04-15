package com.eventpaiger.authentication;

import com.eventpaiger.user.model.user.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationProviderImpl implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserProfile userDetails = loadUserToContext(authentication);
        if(validCredentials(userDetails, authentication)){
            return new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        } else {
            log.warn("LOGIN FAILED");
            throw new RuntimeException("LOGIN FAILED");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean validCredentials(UserProfile userDetails, Authentication authentication) {
        return passwordEncoder.matches(getPass(authentication), userDetails.getPassword());
    }

    private CharSequence getPass(Authentication authentication) {
        final var credentials = authentication.getCredentials();
        if(credentials == null){
            return null;
        }
        return credentials.toString();
    }

    private UserProfile loadUserToContext(Authentication authentication) {
        return (UserProfile) userDetailsService.loadUserByUsername(authentication.getName());
    }

}
