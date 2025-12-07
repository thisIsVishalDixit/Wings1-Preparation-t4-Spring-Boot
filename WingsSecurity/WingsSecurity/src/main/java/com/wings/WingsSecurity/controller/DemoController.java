package com.wings.WingsSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wings.WingsSecurity.util.JwtUtil;

@RestController
@RequestMapping("/api")
public class DemoController {

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/hello")
    public String getMessage(@RequestHeader("Authorization") String authHeader) {
        String username = jwtUtil.extractUsername(authHeader.substring(7));
        return "Server Says hello : " + username;
    }
}
