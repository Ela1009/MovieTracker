package com.example.movieapp.controllers;

import com.example.movieapp.models.Movie;
import com.example.movieapp.services.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/movies")
public class AdminMovieController {

    private final MovieService movieService;

    public AdminMovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/add")
    public String showAddMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "/admin/add-movie";
    }

    @PostMapping("/add")
    public String addMovie(@ModelAttribute Movie movie) {
        movieService.addMovie(movie);
        return "redirect:/admin/movies/add?success";
    }

    @GetMapping("/all")
    public String viewAllMovies(Model model) {
        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "admin/view-movies";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Movie> searchMovies(@RequestParam(required = false) String title,
                                    @RequestParam(required = false) String director,
                                    @RequestParam(required = false) String genre,
                                    @RequestParam(required = false) Integer year) {
        return movieService.searchMovies(title, director, genre, year);
    }


}

