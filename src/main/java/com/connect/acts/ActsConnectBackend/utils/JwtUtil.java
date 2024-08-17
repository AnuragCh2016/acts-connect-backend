package com.connect.acts.ActsConnectBackend.utils;

import com.connect.acts.ActsConnectBackend.model.UserType;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
  private final String SECRET_KEY;
  private final int JWT_EXPIRY;

  public JwtUtil() {
    final Dotenv dotenv = Dotenv.load();
      this.SECRET_KEY = dotenv.get("JWT_SECRET_KEY");
      this.JWT_EXPIRY = Integer.parseInt(dotenv.get("JWT_EXPIRY"));
  }

  public String generateToken(final UUID id, final String email, final UserType userType) {
    return Jwts.builder()
      .claim("id", id.toString())
      .claim("email", email)
      .claim("userType", userType.name())
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + this.JWT_EXPIRY))
      .signWith(Keys.hmacShaKeyFor(this.SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
      .compact();
  }

  public Claims extractClaims(String token) {
    if (token.startsWith("Bearer ")) {
      token = token.substring(7);
    }
    return Jwts.parserBuilder()
      .setSigningKey(Keys.hmacShaKeyFor(this.SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  public UUID extractUserId(final String token) {
    final Claims claims = this.extractClaims(token);
    return UUID.fromString(claims.get("id", String.class));
  }

  public String extractEmail(final String token) {
    final Claims claims = this.extractClaims(token);
    return claims.get("email", String.class);
  }

  public UserType extractUserType(final String token) {
    final Claims claims = this.extractClaims(token);
    final String userTypeString = claims.get("userType", String.class);
    return UserType.valueOf(userTypeString);
  }

  public boolean isTokenExpired(final String token) {
    final Claims claims = this.extractClaims(token);
    return claims.getExpiration().before(new Date());
  }

  public boolean validateToken(final String token, final UUID userId) {
    final Claims claims = this.extractClaims(token);
    return claims.get("id", String.class).equals(userId.toString()) && !this.isTokenExpired(token);
  }

  public boolean validateToken(final String token, final String email) {
    final Claims claims = this.extractClaims(token);
    return claims.get("email", String.class).equals(email) && !this.isTokenExpired(token);
  }
}
