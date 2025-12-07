package com.wings.WingsWeather.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.wings.WingsWeather.entity.Role;
import com.wings.WingsWeather.entity.Users;
import com.wings.WingsWeather.repository.UserDetailsRepository;

@Component
public class AdminUserInitializer {

    @Bean
    public CommandLineRunner createAdminUser(UserDetailsRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Users admin = new Users();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin1234"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("Default admin user created !");
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                Users user = new Users();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user1234"));
                user.setRole(Role.USER);

                userRepository.save(user);

                System.out.println("Default User user created !");
            }
        };
    }

}
