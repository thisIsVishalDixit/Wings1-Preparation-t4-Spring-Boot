package com.example.t4_ecommerce_springboot.filter;

import com.example.t4_ecommerce_springboot.config.*;
import com.example.t4_ecommerce_springboot.service.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Import required Annotations and implement the  business logics
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authhead=request.getHeader("Authorization");
        String token=null;
        String username=null;
        if(authhead!=null && authhead.startsWith("Bearer ")){
            token=authhead.substring(7);
            username=jwtService.extractUsername(token);
            UserDetails userinfo = userDetailsService.loadUserByUsername(username);
            if(jwtService.validateToken(token, userinfo)){
                UsernamePasswordAuthenticationToken authenticationToken=new 
                UsernamePasswordAuthenticationToken(userinfo.getUsername(), null,userinfo.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);

        
    }
}