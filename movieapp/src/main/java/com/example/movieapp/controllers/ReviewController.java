package com.example.movieapp.controllers;

import com.example.movieapp.models.Review;
import com.example.movieapp.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/critic/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/write")
    public String showReviewForm(Model model) {
        model.addAttribute("movies", reviewService.getAllMovies());
        model.addAttribute("review", new Review());
        return "critic/write-review";
    }

    @PostMapping("/submit")
    public String submitReview(@ModelAttribute Review review,
                               @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.submitReview(review, userDetails);
        return "redirect:/critic/dashboard";
    }

    @GetMapping("/movie/{movieId}")
    public String viewReviewsForMovie(@PathVariable Long movieId, Model model) {
        model.addAttribute("movie", reviewService.getMovie(movieId));
        model.addAttribute("reviews", reviewService.getReviewsForMovie(movieId));
        return "movie-reviews";
    }

    @GetMapping("/my-reviews")
    public String viewMyReviews(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("myReviews", reviewService.getReviewsByCritic(userDetails));
        return "critic/my-reviews";
    }

    @GetMapping("/latest")
    public String showLatestReview(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("latestReview", reviewService.getLatestReviewByCritic(userDetails));
        return "critic-dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.deleteReviewByIdIfOwner(id, userDetails.getUsername());
        return "redirect:/critic/reviews/my-reviews";
    }
}
