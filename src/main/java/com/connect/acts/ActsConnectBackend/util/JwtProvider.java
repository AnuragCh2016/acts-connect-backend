package com.connect.acts.ActsConnectBackend.util;

import com.connect.acts.ActsConnectBackend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${jwt.refreshExpiration}")
    private long jwtRefreshExpirationMs;

    public JwtProvider(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());  // Generate a SecretKey from the secret string
    }

    public String generateAccessToken(User user) {
        return Jwts.builder().subject(user.getEmail()).issuedAt(new Date()).expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey())  // Use SecretKey for signing
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder().subject(user.getEmail()).issuedAt(new Date()).expiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs))
                .signWith(getSigningKey())  // Use SecretKey for signing
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            // Convert the secret key string to a SecretKey using Keys.hmacShaKeyFor
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            // Create the JwtParser instance and parse the token
            Jwts.parserBuilder()  // Use parserBuilder to create a JwtParserBuilder
                    .setSigningKey(key)  // Set the signing key using SecretKey
                    .build()  // Build the JwtParser
                    .parseClaimsJws(token);  // Parse the JWS token

            return true;
        } catch (Exception e) {
            // Log token validation failure
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8); // Convert the secret key string to a byte array
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes)) // Use Keys.hmacShaKeyFor to create the key
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
