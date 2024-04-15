package com.eventpaiger.config;

import com.eventpaiger.user.model.user.UserProfile;
import com.eventpaiger.security.AuthenticationToken;
import com.eventpaiger.security.JwtAuthenticationConverter;
import com.eventpaiger.security.TokenClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final long jwtExpiration;

    public JwtService(JwtEncoder jwtEncoder,
                      JwtDecoder jwtDecoder,
                      @Value("${application.security.jwt.expiration}") long jwtExpiration) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.jwtExpiration = jwtExpiration;
    }

    public AuthenticationToken decodeToken(String token){
        Jwt jwt = jwtDecoder.decode(token);
        return JwtAuthenticationConverter.convert(jwt);
    }

    public TokenClaims getTokenClaims(AuthenticationToken authenticationToken) {
        return authenticationToken.getTokenClaims();
    }

    public boolean isTokenExpired(AuthenticationToken token) {
        return token.getExpirationDate().isBefore(Instant.now());
    }

    public String generateToken(UserProfile userProfile){
        return buildToken(userProfile, jwtExpiration);
    }

    private String buildToken(UserProfile userProfile, long expiration) {

        String authoritiesAndRoles = getAuthoritiesAndRolesToString(userProfile);

        JwtClaimsSet claims = buildJwtClaims(userProfile, expiration, authoritiesAndRoles);
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private static String getAuthoritiesAndRolesToString(UserProfile userProfile) {
        return userProfile.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }

    private static JwtClaimsSet buildJwtClaims(UserProfile userProfile, long expiration, String authoritiesAndRoles) {
        Instant generationDate = Instant.now();
        boolean eventOrganizerIdString = Optional.ofNullable(userProfile.getEventOrganizerId()).isPresent();

        return eventOrganizerIdString
                ? buildClaimsForOrganizer(userProfile, expiration, authoritiesAndRoles, generationDate)
                : buildClaimsForObserver(userProfile, expiration, authoritiesAndRoles, generationDate);
    }

    private static JwtClaimsSet buildClaimsForOrganizer(UserProfile userProfile, long expiration, String authoritiesAndRoles, Instant generationDate) {
        return JwtClaimsSet.builder()
                .issuer("event-pager-auth")
                .issuedAt(generationDate)
                .expiresAt(Instant.ofEpochMilli(generationDate.toEpochMilli() + expiration))
                .subject(userProfile.getUsername())
                .claim("email", userProfile.getEmail())
                .claim("event_organizer_id", userProfile.getEventOrganizerId())
                .claim("scope", authoritiesAndRoles)
                .build();
    }

    private static JwtClaimsSet buildClaimsForObserver(UserProfile userProfile, long expiration, String authoritiesAndRoles, Instant generationDate) {
        return JwtClaimsSet.builder()
                .issuer("event-pager-auth")
                .issuedAt(generationDate)
                .expiresAt(Instant.ofEpochMilli(generationDate.toEpochMilli() + expiration))
                .subject(userProfile.getUsername())
                .claim("email", userProfile.getEmail())
                .claim("scope", authoritiesAndRoles)
                .build();
    }
}
