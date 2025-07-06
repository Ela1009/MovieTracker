package com.example.movieapp.controllers;

import com.example.movieapp.models.Review;
import com.example.movieapp.models.User;
import com.example.movieapp.repositories.ReviewRepository;
import com.example.movieapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CriticDashboardController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/critic/dashboard")
    public String criticDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        model.addAttribute("username", username);
        User critic = userRepository.findByUsername(userDetails.getUsername());
        Review latestReview = reviewRepository.findTopByCriticOrderByCreatedAtDesc(critic);
        model.addAttribute("latestReview", latestReview);
        return "critic-dashboard";
    }
}
