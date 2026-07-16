package com.denizbyrk.sffecommerce.user_service.config;

import com.denizbyrk.sffecommerce.user_service.entity.Role;
import com.denizbyrk.sffecommerce.user_service.entity.User;
import com.denizbyrk.sffecommerce.user_service.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@xxx.com");
                admin.setPassword(passwordEncoder.encode("1234"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("Admin user created: username = admin, email = admin@xxx.com, password = 1234");

            } else {

                System.out.println("Admin user already exists, skipping creation.\nusername = admin, email = admin@xxx.com, password = 1234\"");
            }

            if (userRepository.findByUsername("test").isEmpty()) {

                User test = new User();
                test.setUsername("test");
                test.setEmail("test@xxx.com");
                test.setPassword(passwordEncoder.encode("1234"));
                test.setRole(Role.USER);

                userRepository.save(test);

                System.out.println("Test user created: username = test, email = test@xxx.com, password = 1234");

            } else {

                System.out.println("Test user already exists, skipping creation.\nusername = test, email = test@xxx.com, password = 1234\"");
            }
        };
    }
}