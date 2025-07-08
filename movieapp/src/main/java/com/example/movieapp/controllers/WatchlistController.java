package com.example.movieapp.controllers;

import com.example.movieapp.models.Movie;
import com.example.movieapp.services.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private WatchlistService watchlistService;

    @GetMapping("/watchlist")
    public String viewWatchlist(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Movie> watchlist = watchlistService.getWatchlistMovies(userDetails);
        model.addAttribute("watchlist", watchlist);
        return "user/watchlist";
    }

    @PostMapping("/watchlist/remove/{movieId}")
    public String removeFromWatchlist(@PathVariable Long movieId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        watchlistService.removeMovieFromUserWatchlist(movieId, userDetails);
        return "redirect:/watchlist";
    }
}
