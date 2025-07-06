package com.example.movieapp.controllers;

import com.example.movieapp.models.Movie;
import com.example.movieapp.models.User;
import com.example.movieapp.models.Watchlist;
import com.example.movieapp.repositories.MovieRepository;
import com.example.movieapp.repositories.UserRepository;
import com.example.movieapp.services.MovieService;
import com.example.movieapp.services.WatchlistService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final WatchlistService watchlistService;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public MovieController(MovieService movieService,
                           WatchlistService watchlistService,
                           UserRepository userRepository,
                           MovieRepository movieRepository) {
        this.movieService = movieService;
        this.watchlistService = watchlistService;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public String viewAndFilterMovies(@RequestParam(value = "keyword", required = false) String keyword,
                                      @RequestParam(value = "director", required = false) String director,
                                      Model model,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());

        List<Movie> movies;

        if ((keyword != null && !keyword.isEmpty()) || (director != null && !director.isEmpty())) {
            movies = movieService.searchByTitleAndDirector(keyword, director);
        } else {
            movies = movieService.getAllMovies();
        }

        List<Movie> userWatchlist = watchlistService.getWatchlistForUser(user)
                .stream().map(Watchlist::getMovie).toList();

        model.addAttribute("movies", movies);
        model.addAttribute("watchlistMovies", userWatchlist);
        model.addAttribute("keyword", keyword);
        model.addAttribute("director", director);

        return "user/movies";
    }



    @PostMapping("/add-to-watchlist/{movieId}")
    public String addToWatchlist(@PathVariable Long movieId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());


        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        watchlistService.addToWatchlist(user, movie);
        return "redirect:/movies";
    }

    @PostMapping("/remove-from-watchlist/{movieId}")
    public String removeFromWatchlist(@PathVariable Long movieId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());


        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        watchlistService.removeFromWatchlist(user, movie);
        return "redirect:/movies";
    }



}
