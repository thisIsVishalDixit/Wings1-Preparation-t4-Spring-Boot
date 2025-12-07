package com.wings.WingsSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.wings.WingsSecurity.entity.Role;
import com.wings.WingsSecurity.entity.User;
import com.wings.WingsSecurity.repository.RoleRepository;
import com.wings.WingsSecurity.repository.UserRepository;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role role1 = new Role();
        role1.setRole("ADMIN");
        Role role2 = new Role();
        role2.setRole("USER");

        // Saving default roles
        Role savedRole1 = roleRepository.save(role1);
        Role savedRole2 = roleRepository.save(role2);

        User user1 = new User();
        user1.setEmail("sheelu@gmail.com");
        user1.setPass("123");
        user1.setRole(savedRole1);

        User user2 = new User();
        user2.setEmail("himanshu@gmail.com");
        user2.setPass("456");
        user2.setRole(savedRole2);

        User user3 = new User();
        user3.setEmail("ashish@gmail.com");
        user3.setPass("789");
        user3.setRole(savedRole2);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

    }

}
