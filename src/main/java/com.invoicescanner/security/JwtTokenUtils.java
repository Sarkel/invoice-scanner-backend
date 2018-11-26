package com.invoicescanner.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtils {

  @Value("jwt.secret")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  public String generateToken(SecurityUser securityUser) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("sub", securityUser.getId());
    claims.put("passwordChangeAdvised", false);
    claims.put("created", getCurrentDate());
    claims.put("scopes", null);
    return buildToken(claims);
  }

  private String buildToken(Map<String, Object> claims) {
    return Jwts.builder()
      .setClaims(claims)
      .setExpiration(generateExpirationDate())
      .signWith(SignatureAlgorithm.HS256, secret)
      .compact();
  }

  public String getUserIdFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  private Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) throws JwtException {
    Claims claims;
    try {
      claims = Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .getBody();
    } catch (final MalformedJwtException | UnsupportedJwtException | SignatureException | ExpiredJwtException |
      IllegalArgumentException e) {
      throw new JwtException("Invalid token", e);
    }
    return claims;
  }

  private Date getCurrentDate() {
    return new Date(System.currentTimeMillis());
  }

  private Date generateExpirationDate() {
    return new Date(System.currentTimeMillis() + this.expiration * 1000);
  }

  public Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(getCurrentDate());
  }
}
