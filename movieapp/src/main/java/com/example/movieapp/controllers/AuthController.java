package com.example.movieapp.controllers;

import com.example.movieapp.models.User;
import com.example.movieapp.models.enums.UserRole;
import com.example.movieapp.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // GET mapping za prikaz login forme
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // GET mapping za prikaz register forme
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    // POST za registraciju
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           HttpServletRequest request) {

        if (userRepository.existsByUsername(username)) {
            request.setAttribute("error", "Username already exists");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.USER);
        userRepository.save(user);

        request.setAttribute("message", "Registration successful. Please log in.");
        return "login";
    }

    // POST za login
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request) {

        UsernamePasswordAuthenticationToken authReq =
                new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication auth = authenticationManager.authenticate(authReq);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);

            User user = userRepository.findByUsername(username);

            switch (user.getRole()) {
                case ADMIN:
                    return "redirect:/admin/dashboard";
                case CRITIC:
                    return "redirect:/critic/dashboard";
                case USER:
                default:
                    return "redirect:/user/dashboard";
            }

        } catch (AuthenticationException ex) {
            request.setAttribute("error", "Invalid credentials");
            return "login";
        }
    }


    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
}
