package com.example.movieapp.controllers;

import com.example.movieapp.models.Movie;
import com.example.movieapp.services.MovieService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class AdminDashboardController {

    private final MovieService movieService;

    public AdminDashboardController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        model.addAttribute("username", username);
        Optional<Movie> lastAddedMovie = movieService.getLastAddedMovie();
        lastAddedMovie.ifPresent(movie -> model.addAttribute("recentMovie", movie));
        return "admin-dashboard";
    }
}
