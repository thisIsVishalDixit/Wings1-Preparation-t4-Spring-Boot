package com.example.t4_ecommerce_springboot.controller;

import com.example.t4_ecommerce_springboot.dto.AuthRequest;
import com.example.t4_ecommerce_springboot.dto.JwtResponse;
import com.example.t4_ecommerce_springboot.service.JwtService;

import java.net.http.HttpClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Import required Annotations and implement the  business logics
@RequestMapping("/api/public")
@RestController
public class LoginController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
      Authentication a = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
      if(a.isAuthenticated()){
        String token = jwtService.generateToken(authRequest.getUsername());
        return ResponseEntity.status(200).body(new JwtResponse(token,200));
      }
      else{
        throw new UsernameNotFoundException(authRequest.getUsername());
      }
    }
}

