package com.ecommerce.website.movie.service;

import com.ecommerce.website.movie.model.Rating;
import com.ecommerce.website.movie.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    public double calculateAudienceScore(Long movieId) {
        List<Rating> ratings = ratingRepository.findByMovieId(movieId);
        if (ratings.isEmpty()) {
            return 0.0;
        }

        long positiveRatings = ratings.stream()
                .filter(rating -> rating.getEvaluation() >= 3.5)
                .count();

        return (positiveRatings / (double) ratings.size()) * 100.0;
    }

    public double calculateAverageRating(Long movieId) {
        List<Rating> ratings = ratingRepository.findByMovieId(movieId);
        if (ratings.isEmpty()) {
            return 0.0;
        }

        double sum = ratings.stream()
                .mapToDouble(Rating::getEvaluation)
                .sum();

        return sum / ratings.size();
    }

    public boolean isMoviePositivelyRated(Long movieId) {
        double audienceScore = calculateAudienceScore(movieId);
        return audienceScore >= 60.0;
    }
}
