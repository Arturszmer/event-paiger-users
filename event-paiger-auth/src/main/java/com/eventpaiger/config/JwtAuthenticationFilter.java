package com.eventpaiger.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.eventpaiger.security.AuthenticationToken;
import com.eventpaiger.security.TokenClaims;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.eventpaiger.auth.AuthConstants.STARTS_WITH_BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwtToken;
        final AuthenticationToken authenticationToken;
        if(authHeader == null || !authHeader.startsWith(STARTS_WITH_BEARER)){
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.replace(STARTS_WITH_BEARER, "");
        authenticationToken = jwtService.decodeToken(jwtToken);
        if(authenticationToken != null && SecurityContextHolder.getContext().getAuthentication() == null){
            TokenClaims principal = jwtService.getTokenClaims(authenticationToken);

            if(jwtService.isTokenExpired(authenticationToken)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        }
    }

}
