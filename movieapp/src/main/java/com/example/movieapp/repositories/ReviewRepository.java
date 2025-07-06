package com.example.movieapp.repositories;

import com.example.movieapp.models.Review;
import com.example.movieapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMovieId(Long movieId);
    List<Review> findByCritic(User critic);
    Review findTopByCriticOrderByCreatedAtDesc(User critic);


}
