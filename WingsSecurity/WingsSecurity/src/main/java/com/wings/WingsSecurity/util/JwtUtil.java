package com.wings.WingsSecurity.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@SuppressWarnings("deprecation")
public class JwtUtil {
    private final static String SECRET_KEY = "ThisIsASecretKeyThisIsASecretKeyThisIsASecretKey";

    private final static long EXPIRATION = 24 * 60 * 60 * 1000l;

    // Decoding/parsing
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

    // Encoding/Generation
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, SECRET_KEY).addClaims(new HashMap<>())
                .setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)).compact();

    }

    // Validating
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        Date expiry = extractExpiry(token);

        if (username.equals(userDetails.getUsername()) && expiry.after(new Date(System.currentTimeMillis()))) {
            return true;
        } else {
            return false;
        }
    }

    // public static void main(String args[]) {
    // UserDetails user = new User("sheelu", "123", Set.of(new
    // SimpleGrantedAuthority("ADMIN")));
    // JwtUtil jwtUtil = new JwtUtil();
    // String token = jwtUtil.generateToken(user);
    // System.err.println("Token : " + token);
    // System.err.println("Username : " + jwtUtil.extractUsername(token));
    // System.err.println("Expiry : " + jwtUtil.extractExpiry(token));
    // System.err.println("Valid : " + jwtUtil.validateToken(token, user));

    // }

}
