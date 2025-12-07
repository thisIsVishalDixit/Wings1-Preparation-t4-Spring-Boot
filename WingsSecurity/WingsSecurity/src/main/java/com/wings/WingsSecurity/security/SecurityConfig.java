package com.wings.WingsSecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Autowired
    AuthenticationProvider authProvider;

    @Autowired
    AuthenticationEntryPoint authEntryPoint;

    private final String[] WHITE_LISTED_URL = { "/login" };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // Step 1 : Disable CSRF
        httpSecurity.csrf(csrf -> csrf.disable());

        // Step 2 : Change the session mangement policy
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Step 3 : register custom filter
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // register

        // Public Endpoints
        httpSecurity.authenticationProvider(authProvider);

        // Protected resources
        httpSecurity.authorizeHttpRequests(httpConfig -> {
            httpConfig.requestMatchers(WHITE_LISTED_URL).permitAll().requestMatchers("/api/**").hasAnyAuthority("USER")

                    // POST /leave/user
                    .requestMatchers(HttpMethod.POST, "/leave/user").hasAnyAuthority("USER")
                    // GET /leave/user
                    .requestMatchers(HttpMethod.GET, "/leave/user").hasAnyAuthority("USER", "ADMIN")
                    // PUT /leave/comment
                    .requestMatchers(HttpMethod.PUT, "/leave/comment").hasAnyAuthority("USER")
                    // PUT /leave/status
                    .requestMatchers(HttpMethod.PUT, "/leave/status").hasAnyAuthority("ADMIN")
                    // DELETE /leave/{id}
                    .requestMatchers(HttpMethod.DELETE, "/leave/{id}").hasAnyAuthority("ADMIN")
                    // GET /leave/days
                    .requestMatchers(HttpMethod.GET, "/leave/days").hasAnyAuthority("ADMIN")
                    .anyRequest().authenticated();
        });

        // add exception handler
        httpSecurity.exceptionHandling(exceptionConfig -> exceptionConfig.authenticationEntryPoint(authEntryPoint));
        return httpSecurity.build();
    }
}
