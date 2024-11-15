package com.eco_energy.config;


import com.eco_energy.model.Role;
import com.eco_energy.model.Customer;
import com.eco_energy.repository.CustomerRepository;
import com.eco_energy.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(CustomerRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setLabel("Admin");
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setLabel("User");
            roleRepository.save(userRole);

            Customer admin = new Customer(
                    "admin",
                    "admin@email",
                    passwordEncoder.encode("admin"),
                    Set.of(adminRole)
            );
            userRepository.save(admin);

            Customer user = new Customer(
                    "user1",
                    "user1@email",
                    passwordEncoder.encode("password"),
                    Set.of(userRole)
            );
            userRepository.save(user);
        };
    }
}
