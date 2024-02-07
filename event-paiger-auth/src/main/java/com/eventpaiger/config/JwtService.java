package com.eventpaiger.config;

import com.eventpaiger.user.model.UserProfile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
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

    private final String PUBLIC_KEY;

    public JwtService(@Value("${spring.security.oauth2.resourceserver.jwt.public-key-location}") String publicKey) {
        this.PUBLIC_KEY = publicKey;
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
        Date generationDate = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .claims().add(extraClaims).and()
                .subject(userProfile.getUsername())
                .issuedAt(generationDate)
                .expiration(new Date(generationDate.getTime() + 24*60*1000))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(PUBLIC_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
