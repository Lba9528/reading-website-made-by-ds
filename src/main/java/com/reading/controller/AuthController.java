package com.reading.controller;

import com.reading.model.Role;
import com.reading.model.User;
import com.reading.repository.RoleRepository;
import com.reading.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam(required = false) String displayName,
                               Model model) {
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "邮箱已被注册");
            return "register";
        }

        Role userRole = roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(new Role("USER")));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .displayName(displayName != null && !displayName.isEmpty() ? displayName : username)
                .roles(roles)
                .build();

        userRepository.save(user);
        return "redirect:/login?registered=true";
    }
}