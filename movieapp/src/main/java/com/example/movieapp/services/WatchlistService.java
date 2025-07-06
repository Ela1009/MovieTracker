package com.example.movieapp.services;

import com.example.movieapp.models.Movie;
import com.example.movieapp.models.User;
import com.example.movieapp.models.Watchlist;
import com.example.movieapp.repositories.WatchlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;

    public WatchlistService(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    public void addToWatchlist(User user, Movie movie) {
        if (watchlistRepository.findByUserAndMovie(user, movie).isEmpty()) {
            watchlistRepository.save(new Watchlist(null, user, movie));
        }
    }

    public void removeFromWatchlist(User user, Movie movie) {
        watchlistRepository.findByUserAndMovie(user, movie)
                .ifPresent(watchlistRepository::delete);
    }

    public List<Watchlist> getWatchlistForUser(User user) {
        return watchlistRepository.findByUser(user);
    }



}
