package com.eco_energy.config;

import com.eco_energy.model.Customer;
import com.eco_energy.model.Role;
import com.eco_energy.repository.CustomerRepository;
import com.eco_energy.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initializeRoles() {
        Role adminRole = new Role("ROLE_ADMIN", "Administrador");
        Role userRole = new Role("ROLE_USER", "Usuário");
        Customer customer = new Customer(
            "admin",
            "admin@admin.com",
            passwordEncoder.encode("admin")
        );

        if (roleRepository.count() == 0) {
            roleRepository.save(adminRole);
            roleRepository.save(userRole);
        }

        if (customerRepository.count() == 0) {
            customer.setRoles(Set.of(adminRole));
            customerRepository.save(customer);
        }
    }

}
