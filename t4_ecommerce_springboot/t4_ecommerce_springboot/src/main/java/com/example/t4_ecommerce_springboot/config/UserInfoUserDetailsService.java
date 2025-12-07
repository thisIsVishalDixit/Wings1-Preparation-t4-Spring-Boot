package com.example.t4_ecommerce_springboot.config;

import com.example.t4_ecommerce_springboot.models.UserInfo;
import com.example.t4_ecommerce_springboot.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
// Import required Annotations and implement the  business logics
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> user = repository.findByUsername(username);
        return user.map(UserInfoUserDetails::new).orElseThrow(()-> new UsernameNotFoundException(username));
    }
}