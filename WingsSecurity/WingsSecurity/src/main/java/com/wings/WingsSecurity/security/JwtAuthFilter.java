package com.wings.WingsSecurity.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wings.WingsSecurity.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Extract Auth Header

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Proceed with remaining filters
            filterChain.doFilter(request, response);
            return;
        }

        // Jwt Purpose
        String jwtToken = authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);
        // User not yet Authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDB = userDetailsService.loadUserByUsername(username);

            // Validate Token
            if (userDB != null && jwtUtil.validateToken(jwtToken, userDB)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDB, null, userDB.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetails(request));

                // update my user as valid user
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // continue filter chain

        filterChain.doFilter(request, response);

    }

}
