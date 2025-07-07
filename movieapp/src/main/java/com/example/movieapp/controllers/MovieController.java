package com.example.movieapp.controllers;

import com.example.movieapp.models.Movie;
import com.example.movieapp.models.User;
import com.example.movieapp.models.Watchlist;
import com.example.movieapp.repositories.MovieRepository;
import com.example.movieapp.repositories.UserRepository;
import com.example.movieapp.services.MovieService;
import com.example.movieapp.services.WatchlistService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final WatchlistService watchlistService;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public MovieController(MovieService movieService,
                           WatchlistService watchlistService,
                           UserRepository userRepository,
                           MovieRepository movieRepository) {
        this.movieService = movieService;
        this.watchlistService = watchlistService;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    /*Ova metoda se aktivira kad korisnik otvori /movies (GET zahtjev).
    Thymeleaf poziva ovu metodu da renderira HTML stranicu. */
    @GetMapping
    public String viewAndFilterMovies(
            @RequestParam(value = "title", required = false) String title, /*Ovi parametri dolaze iz URL-a (npr. ?title=Matrix&genre=Sci-Fi) */
            @RequestParam(value = "director", required = false) String director,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername());

        Page<Movie> moviePage = movieService.searchMoviesPaginated(title, director, genre, year, page, size);

        List<Movie> userWatchlist = watchlistService.getWatchlistForUser(user)
                .stream().map(Watchlist::getMovie).toList();

        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());

        model.addAttribute("watchlistMovies", userWatchlist);

        // za ponovno prikazivanje unesenih inputa u search formi
        model.addAttribute("title", title);
        model.addAttribute("director", director);
        model.addAttribute("genre", genre);
        model.addAttribute("year", year);

        return "user/movies";
    }




    @PostMapping("/add-to-watchlist/{movieId}")
    public String addToWatchlist(@PathVariable Long movieId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());


        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        watchlistService.addToWatchlist(user, movie);
        return "redirect:/movies";
    }

    @PostMapping("/remove-from-watchlist/{movieId}")
    public String removeFromWatchlist(@PathVariable Long movieId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());


        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        watchlistService.removeFromWatchlist(user, movie);
        return "redirect:/movies";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Movie> searchMoviesForUser(@RequestParam(required = false) String title,
                                           @RequestParam(required = false) String director,
                                           @RequestParam(required = false) String genre,
                                           @RequestParam(required = false) Integer year) {
        return movieService.searchMovies(title, director, genre, year);
    }


}
