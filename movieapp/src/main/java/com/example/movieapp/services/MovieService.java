package com.example.movieapp.services;

import com.example.movieapp.models.Movie;
import com.example.movieapp.repositories.MovieRepository;
import org.springframework.stereotype.Service;

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

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getLastAddedMovie() {
        return movieRepository.findTopByOrderByIdDesc(); // koristi ako je id auto-increment
    }

    public List<Movie> searchByTitle(String keyword) {
        return movieRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Movie> searchByTitleAndDirector(String keyword, String director) {
        if ((keyword == null || keyword.isEmpty()) && (director == null || director.isEmpty())) {
            return movieRepository.findAll();
        }
        if (keyword != null && !keyword.isEmpty() && director != null && !director.isEmpty()) {
            return movieRepository.findByTitleContainingIgnoreCaseAndDirectorContainingIgnoreCase(keyword, director);
        }
        if (keyword != null && !keyword.isEmpty()) {
            return movieRepository.findByTitleContainingIgnoreCase(keyword);
        }
        return movieRepository.findByDirectorContainingIgnoreCase(director);
    }

}

