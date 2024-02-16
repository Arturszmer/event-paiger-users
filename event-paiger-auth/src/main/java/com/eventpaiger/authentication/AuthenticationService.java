package com.eventpaiger.authentication;

import com.eventpaiger.config.JwtService;
import com.eventpaiger.dto.auth.AuthenticationRequest;
import com.eventpaiger.dto.auth.AuthenticationResponse;
import com.eventpaiger.dto.auth.RegistrationRequest;
import com.eventpaiger.user.model.*;
import com.eventpaiger.user.repository.TokenRepository;
import com.eventpaiger.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        UserProfile userProfile = UserProfile.forRegistration(
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

    private void saveToken(String generatedToken, UserProfile savedUser) {
        Token token = Token.generateToken(savedUser, generatedToken);
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.usernameOrEmail(), request.password()));

        UserProfile user = findUser(request);
        String generatedToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveToken(generatedToken, user);

        return new AuthenticationResponse(generatedToken, refreshToken);
    }

    private void revokeAllUserTokens(UserProfile user) {
        List<Token> allValidTokens = tokenRepository.findAllValidTokens(user.getId());
        if(allValidTokens.isEmpty()){
            return;
        }
        allValidTokens.forEach(Token::terminate);
        tokenRepository.saveAll(allValidTokens);
    }

    private UserProfile findUser(AuthenticationRequest request) {
        return userProfileRepository.findUserProfileByUsername(request.usernameOrEmail())
                .orElseGet(() -> userProfileRepository.findUserProfileByEmail(request.usernameOrEmail())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }
}
