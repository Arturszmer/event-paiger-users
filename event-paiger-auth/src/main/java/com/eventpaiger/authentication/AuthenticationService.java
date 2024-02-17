package com.eventpaiger.authentication;

import com.eventpaiger.config.JwtService;
import com.eventpaiger.dto.auth.AuthenticationRequest;
import com.eventpaiger.dto.auth.AuthenticationResponse;
import com.eventpaiger.dto.auth.RegistrationRequest;
import com.eventpaiger.user.model.*;
import com.eventpaiger.user.repository.TokenRepository;
import com.eventpaiger.user.repository.UserProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.eventpaiger.dto.auth.AuthConstants.STARTS_WITH_BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserProfileRepository userProfileRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationResponse register(RegistrationRequest request){
        UserProfile userProfile = UserProfile.createForRegistration(
                request.username(), request.email(), passwordEncoder.encode(request.password())
        );

        if(request.isOrganizer()){
            userProfile.addRole(new Role(RoleType.ORGANIZER));
        }

        UserProfile savedUser = userProfileRepository.save(userProfile);
        String generatedToken = jwtService.generateToken(savedUser);
        String refreshedToken = jwtService.generateRefreshToken(savedUser);
        saveToken(generatedToken, savedUser);
        return new AuthenticationResponse(generatedToken, refreshedToken);

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.usernameOrEmail(), request.password()));

        UserProfile user = findUser(request.usernameOrEmail());
        String generatedToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveToken(generatedToken, user);

        return new AuthenticationResponse(generatedToken, refreshToken);
    }

    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String refreshToken;
        final String usernameOrEmail;
        if(authHeader == null || !authHeader.startsWith(STARTS_WITH_BEARER)){
            return;
        }

        refreshToken = authHeader.replace(STARTS_WITH_BEARER, "");
        usernameOrEmail = jwtService.extractUsername(refreshToken);
        if(usernameOrEmail != null) {
            var user = findUser(usernameOrEmail);

            if (jwtService.isValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveToken(accessToken, user);
                var authResponse = new AuthenticationResponse(
                   accessToken, refreshToken
                );
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }

    }

    private void saveToken(String generatedToken, UserProfile savedUser) {
        Token token = Token.generateToken(savedUser, generatedToken);
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserProfile user) {
        List<Token> allValidTokens = tokenRepository.findAllValidTokens(user.getId());
        if(allValidTokens.isEmpty()){
            return;
        }
        allValidTokens.forEach(Token::terminate);
        tokenRepository.saveAll(allValidTokens);
    }

    private UserProfile findUser(String user) {
        return userProfileRepository.findUserProfileByUsername(user)
                .orElseGet(() -> userProfileRepository.findUserProfileByEmail(user)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }
}
