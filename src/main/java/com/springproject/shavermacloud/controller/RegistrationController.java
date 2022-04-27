package com.springproject.shavermacloud.controller;

import com.springproject.shavermacloud.dao.RegistrationForm;
import com.springproject.shavermacloud.domain.Provider;
import com.springproject.shavermacloud.domain.Role;
import com.springproject.shavermacloud.domain.User;
import com.springproject.shavermacloud.repos.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(
            UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }


    @PostMapping
    public String processRegistration(RegistrationForm form) {
        User user = form.toUser(passwordEncoder);
        user.setRoles(Collections.singleton(Role.USER));
        user.setProvider(Provider.LOCAL);
        userRepo.save(user);
        return "redirect:/login";
    }
}