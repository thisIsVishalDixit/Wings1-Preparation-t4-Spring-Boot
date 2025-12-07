package com.example.t4_ecommerce_springboot.service;

import com.example.t4_ecommerce_springboot.models.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// Import required annotations and add business logics

@Service
public class JwtService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347";
    public static final long JWT_TOKEN_VALIDITY = 900000;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims c=extractAllClaims(token);
        return claimsResolver.apply(c);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
         return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String usrn=extractUsername(token);
        return (usrn.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    public String generateToken(String userName){
        return createToken(new HashMap<>(), userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(userName)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY))
                   .signWith(getSignKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    private Key getSignKey() {
        byte[] b = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(b);
        
    }
}

