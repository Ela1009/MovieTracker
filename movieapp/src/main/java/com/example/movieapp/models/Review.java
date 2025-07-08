package com.example.movieapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User critic;

    @ManyToOne
    private Movie movie;

    private int rating;

    @Column(length = 5000)
    private String reviewText;

    private LocalDateTime createdAt = LocalDateTime.now();
}

