package com.example.movieapp.services;

import com.example.movieapp.models.Movie;
import com.example.movieapp.repositories.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.Predicate;


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

    public Page<Movie> getMoviesPaginated(int page, int size) {
        return movieRepository.findAll(PageRequest.of(page, size));
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

    public Page<Movie> searchMoviesPaginated(String title, String director, String genre, Integer year, int page, int size) {
        Specification<Movie> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

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

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        return movieRepository.findAll(spec, PageRequest.of(page, size));
    }


}

