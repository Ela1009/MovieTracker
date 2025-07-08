package com.example.movieapp.services;

import com.example.movieapp.models.Movie;
import com.example.movieapp.models.User;
import com.example.movieapp.models.Watchlist;
import com.example.movieapp.repositories.MovieRepository;
import com.example.movieapp.repositories.UserRepository;
import com.example.movieapp.repositories.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    private User getUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername());
    }

    public List<Watchlist> getWatchlistForUser(UserDetails userDetails) {
        return watchlistRepository.findByUser(getUser(userDetails));
    }

    public List<Movie> getWatchlistMovies(UserDetails userDetails) {
        return getWatchlistForUser(userDetails)
                .stream()
                .map(Watchlist::getMovie)
                .toList();
    }

    public void addMovieToUserWatchlist(Long movieId, UserDetails userDetails) {
        User user = getUser(userDetails);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        if (watchlistRepository.findByUserAndMovie(user, movie).isEmpty()) {
            watchlistRepository.save(new Watchlist(null, user, movie));
        }
    }

    public void removeMovieFromUserWatchlist(Long movieId, UserDetails userDetails) {
        User user = getUser(userDetails);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        watchlistRepository.findByUserAndMovie(user, movie)
                .ifPresent(watchlistRepository::delete);
    }
}
