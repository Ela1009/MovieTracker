package com.example.movieapp.repositories;
import com.example.movieapp.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findTopByOrderByIdDesc();
    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findByDirectorContainingIgnoreCase(String director);
    List<Movie> findByTitleContainingIgnoreCaseAndDirectorContainingIgnoreCase(String title, String director);



}

