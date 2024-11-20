package com.eco_energy.controller;

import com.eco_energy.dto.customer.UserForm;
import com.eco_energy.model.Role;
import com.eco_energy.repository.RoleRepository;
import com.eco_energy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CustomerController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "customer/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userForm") UserForm userForm) {
        Role userRole = roleRepository.findByName("ROLE_USER");

        userService.saveUser(
                userForm.getUsername(),
                userForm.getEmail(),
                passwordEncoder.encode(userForm.getPassword()),
                userRole);
        return "redirect:customer/register";
    }

    @GetMapping("/admin/register")
    public String showAdminRegistrationForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("userForm", new UserForm());
        return "admin/register";
    }

    @PostMapping("/admin/register")
    public String registerUserAdmin(@ModelAttribute("userForm") UserForm userForm) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        userService.saveUser(
                userForm.getUsername(),
                userForm.getEmail(),
                passwordEncoder.encode(userForm.getPassword()),
                adminRole);
        return "redirect:admin/register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
