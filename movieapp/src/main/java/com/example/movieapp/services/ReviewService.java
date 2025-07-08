package com.example.movieapp.services;

import com.example.movieapp.models.Movie;
import com.example.movieapp.models.Review;
import com.example.movieapp.models.User;
import com.example.movieapp.repositories.MovieRepository;
import com.example.movieapp.repositories.ReviewRepository;
import com.example.movieapp.repositories.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public void submitReview(Review review, UserDetails userDetails) {
        User critic = userRepository.findByUsername(userDetails.getUsername());
        review.setCritic(critic);
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    public List<Review> getReviewsForMovie(Long movieId) {
        return reviewRepository.findByMovieId(movieId);
    }

    public List<Review> getReviewsByCritic(UserDetails userDetails) {
        User critic = userRepository.findByUsername(userDetails.getUsername());
        return reviewRepository.findByCritic(critic);
    }

    public Review getLatestReviewByCritic(UserDetails userDetails) {
        User critic = userRepository.findByUsername(userDetails.getUsername());
        return reviewRepository.findTopByCriticOrderByCreatedAtDesc(critic);
    }

    public void deleteReviewByIdIfOwner(Long reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getCritic().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not the owner of this review");
        }

        reviewRepository.deleteById(reviewId);
    }

    public Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
}
