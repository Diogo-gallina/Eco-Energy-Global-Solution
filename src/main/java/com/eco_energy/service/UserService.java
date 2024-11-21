package com.eco_energy.service;

import com.eco_energy.model.Customer;
import com.eco_energy.model.Role;
import com.eco_energy.repository.RoleRepository;
import com.eco_energy.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private CustomerRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public void saveUser(String username, String email, String password, Role role) {
        Customer customer = new Customer(username, email, password);

        HashSet<Role> userRoles = new HashSet<>();
        Role findedRole = roleRepository.findByName(role.getName());

        if (findedRole == null) throw new UsernameNotFoundException("Role not found");

        userRoles.add(role);
        customer.setRoles(userRoles);
        userRepository.save(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    public Customer getAuthenticatedCustomer(Authentication authentication) {
        String username = authentication.getName();
        Customer customer = userRepository.findByUsername(username);

        if (customer == null) throw new IllegalArgumentException("Cliente não encontrado!");

        return customer;
    }
}
