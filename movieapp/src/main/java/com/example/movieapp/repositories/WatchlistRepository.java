package com.example.movieapp.repositories;

import com.example.movieapp.models.Movie;
import com.example.movieapp.models.User;
import com.example.movieapp.models.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUser(User user);
    Optional<Watchlist> findByUserAndMovie(User user, Movie movie);
}
