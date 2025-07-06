package com.example.movieapp.controllers;

import com.example.movieapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/user-management")
    public String showUserManagement(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-management";
    }

    @PostMapping("/promote/{id}")
    public String promoteUserToCritic(@PathVariable Long id) {
        userService.promoteToCritic(id);
        return "redirect:/admin/user-management";
    }
}

