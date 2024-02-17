package com.eventpaiger.authentication;

import com.eventpaiger.config.JwtService;
import com.eventpaiger.config.SecurityContextHelper;
import com.eventpaiger.user.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import static com.eventpaiger.dto.auth.AuthConstants.STARTS_WITH_BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith(STARTS_WITH_BEARER)){
            log.info("Nothing to logout");
            return;
        }
        String jwtToken = authHeader.replace(STARTS_WITH_BEARER, "");
        String user = jwtService.extractUsername(jwtToken);
        tokenRepository.findByToken(jwtToken).ifPresent(token -> {
            token.terminate();
            tokenRepository.save(token);
            SecurityContextHelper.clearAuthentication();
            log.info("The user {} has been logout successfully", user);
        });
    }
}
