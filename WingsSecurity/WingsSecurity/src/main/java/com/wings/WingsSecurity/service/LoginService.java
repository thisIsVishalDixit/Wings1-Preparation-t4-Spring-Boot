package com.wings.WingsSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wings.WingsSecurity.entity.LoginRequest;
import com.wings.WingsSecurity.entity.LoginResponse;
import com.wings.WingsSecurity.util.JwtUtil;

@Service
public class LoginService {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationProvider authenticationProvider;

    public ResponseEntity<Object> loginUser(LoginRequest loginRequest) {
        try {
            // fetch user by username
            UserDetails userDB = userDetailsService.loadUserByUsername(loginRequest.getEmailId());

            // validate the password
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmailId(), loginRequest.getPassword());

            authenticationProvider.authenticate(authenticationToken);

            String token = jwtUtil.generateToken(userDB);

            return ResponseEntity.ok(new LoginResponse(token));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("User does not exist");
        }
    }
}
