package com.example.movieapp.controllers;

import com.example.movieapp.models.Movie;
import com.example.movieapp.services.MovieService;
import com.example.movieapp.services.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private WatchlistService watchlistService;


    @GetMapping
    public String showMoviesPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("watchlistMovies", watchlistService.getWatchlistMovies(userDetails));
        return "user/movies";
    }

    @PostMapping("/add-to-watchlist/{movieId}")
    public String addToWatchlist(@PathVariable Long movieId, @AuthenticationPrincipal UserDetails userDetails) {
        watchlistService.addMovieToUserWatchlist(movieId, userDetails);
        return "redirect:/movies";
    }

    @PostMapping("/remove-from-watchlist/{movieId}")
    public String removeFromWatchlist(@PathVariable Long movieId, @AuthenticationPrincipal UserDetails userDetails) {
        watchlistService.removeMovieFromUserWatchlist(movieId, userDetails);
        return "redirect:/movies";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Movie> searchMoviesForUser(@RequestParam(required = false) String title,
                                           @RequestParam(required = false) String director,
                                           @RequestParam(required = false) String genre,
                                           @RequestParam(required = false) Integer year) {
        return movieService.searchMovies(title, director, genre, year);
    }
}
