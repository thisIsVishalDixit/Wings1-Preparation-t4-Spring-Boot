package com.wings.WingsWeather.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @GetMapping("/health")
    // @PreAuthorize("hasAuthority('WEATHER_READ')")
    @PreAuthorize("hasAnyRole( 'ADMIN')")
    public String getHealth() {
        return "Healthy";
    }
}
