package com.example.movieapp.controllers;

import com.example.movieapp.models.Movie;
import com.example.movieapp.models.User;
import com.example.movieapp.repositories.ReviewRepository;
import com.example.movieapp.repositories.UserRepository;
import com.example.movieapp.services.MovieService;
import com.example.movieapp.services.UserService;
import com.example.movieapp.services.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class UserDashboardController {

    private final MovieService movieService;

    public UserDashboardController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        model.addAttribute("username", username);

        Optional<Movie> lastAddedMovie = movieService.getLastAddedMovie();
        lastAddedMovie.ifPresent(movie -> model.addAttribute("recentMovie", movie));

        return "user-dashboard";
    }
}
