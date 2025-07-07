package com.example.movieapp.repositories;

import com.example.movieapp.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByTitleContainingIgnoreCaseAndDirectorContainingIgnoreCase(String title, String director);
    List<Movie> findByDirectorContainingIgnoreCase(String director);
    Optional<Movie> findTopByOrderByIdDesc();
}
