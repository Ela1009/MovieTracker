package com.example.movieapp.controllers;

import com.example.movieapp.models.Movie;
import com.example.movieapp.models.User;
import com.example.movieapp.models.Watchlist;
import com.example.movieapp.repositories.MovieRepository;
import com.example.movieapp.repositories.UserRepository;
import com.example.movieapp.repositories.WatchlistRepository;
import com.example.movieapp.services.WatchlistService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class WatchlistController {

    private final UserRepository userRepository;
    private final WatchlistService watchlistService;
    private final MovieRepository movieRepository;

    public WatchlistController(UserRepository userRepository, WatchlistService watchlistService, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.watchlistService = watchlistService;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/watchlist")
    public String viewWatchlist(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());


        List<Movie> watchlist = watchlistService.getWatchlistForUser(user)
                .stream().map(Watchlist::getMovie).toList();

        model.addAttribute("watchlist", watchlist);
        return "user/watchlist";
    }

    @PostMapping("/watchlist/remove/{movieId}")
    public String removeFromWatchlistFromWatchlistPage(@PathVariable Long movieId,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        watchlistService.removeFromWatchlist(user, movie);
        return "redirect:/watchlist";
    }


}
