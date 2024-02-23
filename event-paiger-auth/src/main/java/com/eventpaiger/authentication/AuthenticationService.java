package com.eventpaiger.authentication;

import com.eventpaiger.config.JwtService;
import com.eventpaiger.dto.auth.AuthenticationRequest;
import com.eventpaiger.dto.auth.AuthenticationResponse;
import com.eventpaiger.dto.auth.RegistrationRequest;
import com.eventpaiger.user.model.*;
import com.eventpaiger.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserProfileRepository userProfileRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationResponse register(RegistrationRequest request){
        UserProfile userProfile = UserProfile.createForRegistration(
                request.username(), request.email(), passwordEncoder.encode(request.password())
        );

        if(request.isOrganizer()){
            userProfile.addRole(new Role(RoleType.ORGANIZER));
            userProfile.setEventOrganizerId();
        }

        UserProfile savedUser = userProfileRepository.save(userProfile);
        String generatedToken = jwtService.generateToken(savedUser);
        return new AuthenticationResponse(generatedToken);

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.usernameOrEmail(), request.password()));

        UserProfile user = findUser(request.usernameOrEmail());
        String generatedToken = jwtService.generateToken(user);

        return new AuthenticationResponse(generatedToken);
    }

    private UserProfile findUser(String user) {
        return userProfileRepository.findUserProfileByUsername(user)
                .orElseGet(() -> userProfileRepository.findUserProfileByEmail(user)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }
}
