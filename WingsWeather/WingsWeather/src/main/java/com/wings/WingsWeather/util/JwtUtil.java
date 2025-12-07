package com.wings.WingsWeather.util;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@SuppressWarnings("deprecation")
public class JwtUtil {

    private final static String SECRET_KEY = "ThisIsASecretKeyThisIsASecretKeyThisIsASecretKey";
    private final static long EXPIRY = 24 * 60 * 60 * 1000L;

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public Date extractExpiry(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    public String generateToken(String username) {
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY)).compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        Date expiry = extractExpiry(token);

        return (username.equals(userDetails.getUsername()) && expiry.after(new Date(System.currentTimeMillis())));
    }
}
