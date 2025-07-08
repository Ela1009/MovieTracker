package com.example.movieapp.services;

import com.example.movieapp.models.Movie;
import com.example.movieapp.repositories.MovieRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void addMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public Optional<Movie> getLastAddedMovie() {
        return movieRepository.findTopByOrderByIdDesc();
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> searchMovies(String title, String director, String genre, Integer year) {
        return movieRepository.findAll((Specification<Movie>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), title.toLowerCase() + "%"));
            }
            if (director != null && !director.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("director")), director.toLowerCase() + "%"));
            }
            if (genre != null && !genre.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("genre")), genre.toLowerCase() + "%"));
            }
            if (year != null) {
                predicates.add(cb.equal(root.get("releaseYear"), year));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
