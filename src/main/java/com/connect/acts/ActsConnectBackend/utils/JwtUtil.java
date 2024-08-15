package com.connect.acts.ActsConnectBackend.utils;

import com.connect.acts.ActsConnectBackend.model.UserType;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
  private final String SECRET_KEY;
  private final int JWT_EXPIRY;

  public JwtUtil() {
    Dotenv dotenv = Dotenv.load();
    SECRET_KEY = dotenv.get("JWT_SECRET_KEY");
    JWT_EXPIRY = Integer.parseInt(dotenv.get("JWT_EXPIRY"));
  }

  public String generateToken(UUID id, String email, UserType userType) {
    return Jwts.builder()
      .claim("id", id.toString())
      .claim("email", email)
      .claim("userType", userType.name())
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRY))
      .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
      .compact();
  }

  public Claims extractClaims(String token) {
    if (token.startsWith("Bearer ")) {
      token = token.substring(7);
    }
    return Jwts.parserBuilder()
      .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  public UUID extractUserId(String token) {
    Claims claims = extractClaims(token);
    return UUID.fromString(claims.get("id", String.class));
  }

  public String extractEmail(String token) {
    Claims claims = extractClaims(token);
    return claims.get("email", String.class);
  }

  public UserType extractUserType(String token) {
    Claims claims = extractClaims(token);
    String userTypeString = claims.get("userType", String.class);
    return UserType.valueOf(userTypeString);
  }

  public boolean isTokenExpired(String token) {
    Claims claims = extractClaims(token);
    return claims.getExpiration().before(new Date());
  }

  public boolean validateToken(String token, UUID userId) {
    Claims claims = extractClaims(token);
    return claims.get("id", String.class).equals(userId.toString()) && !isTokenExpired(token);
  }

  public boolean validateToken(String token, String email) {
    Claims claims = extractClaims(token);
    return claims.get("email", String.class).equals(email) && !isTokenExpired(token);
  }
}
