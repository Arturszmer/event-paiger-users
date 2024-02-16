package com.eventpaiger.config;

import com.eventpaiger.user.model.UserProfile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final RsaKeyProperties rsaKeys;
    private final long jwtExpiration;
    private final long refreshTokenTime;


    public JwtService(RsaKeyProperties keyProperties,
                      @Value("${application.security.jwt.expiration}") long jwtExpiration,
                      @Value("${application.security.jwt.refresh}") long refreshTokenTime) {
        this.rsaKeys = keyProperties;
        this.jwtExpiration = jwtExpiration;
        this.refreshTokenTime = refreshTokenTime;
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public boolean isValid(String token, UserDetails user){
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(UserProfile userProfile){
        return generateToken(userProfile, new HashMap<>());
    }

    public String generateToken(UserProfile userProfile, Map<String, Object> extraClaims){
        return buildToken(userProfile, extraClaims, jwtExpiration);
    }

    public String generateRefreshToken(UserProfile savedUser) {
        return buildToken(savedUser, new HashMap<>(), refreshTokenTime);
    }

    private String buildToken(UserProfile userProfile,
                              Map<String, Object> extraClaims,
                              long expiration) {
        Date generationDate = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .claims().add(extraClaims).and()
                .subject(userProfile.getUsername())
                .issuedAt(generationDate)
                .expiration(new Date(generationDate.getTime() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = rsaKeys.publicKey().getEncoded();
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
