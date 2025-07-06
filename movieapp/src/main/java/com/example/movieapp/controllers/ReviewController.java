package com.example.movieapp.controllers;

import com.example.movieapp.models.Movie;
import com.example.movieapp.models.Review;
import com.example.movieapp.models.User;
import com.example.movieapp.repositories.MovieRepository;
import com.example.movieapp.repositories.ReviewRepository;
import com.example.movieapp.repositories.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/critic/reviews")
public class ReviewController {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public ReviewController(MovieRepository movieRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/write")
    public String showReviewForm(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("review", new Review());
        return "critic/write-review";
    }

    @PostMapping("/submit")
    public String submitReview(@ModelAttribute Review review, @AuthenticationPrincipal UserDetails userDetails) {
        User critic = userRepository.findByUsername(userDetails.getUsername());
        review.setCritic(critic);
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
        return "redirect:/critic/dashboard";
    }

    @GetMapping("/movie/{movieId}")
    public String viewReviewsForMovie(@PathVariable Long movieId, Model model) {
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        model.addAttribute("movie", movie);
        model.addAttribute("reviews", reviews);
        return "movie-reviews";
    }

    @GetMapping("/my-reviews")
    public String viewMyReviews(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User critic = userRepository.findByUsername(userDetails.getUsername());
        List<Review> myReviews = reviewRepository.findByCritic(critic);
        model.addAttribute("myReviews", myReviews);
        return "critic/my-reviews";
    }

    @GetMapping("/latest")
    public String showLatestReview(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User critic = userRepository.findByUsername(userDetails.getUsername());
        Review latestReview = reviewRepository.findTopByCriticOrderByCreatedAtDesc(critic);
        model.addAttribute("latestReview", latestReview);
        return "critic-dashboard";
    }

}