package com.wings.WingsSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wings.WingsSecurity.entity.LoginRequest;
import com.wings.WingsSecurity.service.LoginService;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginRequest loginRequest) {
        return loginService.loginUser(loginRequest);
    }
}
