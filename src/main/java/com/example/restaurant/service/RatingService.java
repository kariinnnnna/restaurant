package com.example.restaurant.service;

import com.example.restaurant.entity.Rating;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.repository.RatingRepository;
import com.example.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;

    public Rating save(Rating rating) {
        Rating saved = ratingRepository.save(rating);
        recalculateRestaurantRating(saved.getRestaurantId());
        return saved;
    }

    public void remove(Long id) {
        Rating existing = ratingRepository.findById(id);
        if (existing != null) {
            Long restaurantId = existing.getRestaurantId();
            ratingRepository.remove(id);
            recalculateRestaurantRating(restaurantId);
        }
    }

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public Rating findById(Long id) {
        return ratingRepository.findById(id);
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            return;
        }
        List<Rating> allRatings = ratingRepository.findAll();
        List<Rating> restaurantRatings = allRatings.stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .toList();
        if (restaurantRatings.isEmpty()) {
            restaurant.setUserRating(BigDecimal.ZERO);
        } else {
            BigDecimal sum = restaurantRatings.stream()
                    .map(r -> BigDecimal.valueOf(r.getScore()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal average = sum.divide(
                    BigDecimal.valueOf(restaurantRatings.size()),
                    2,
                    RoundingMode.HALF_UP
            );

            restaurant.setUserRating(average);
        }

        restaurantRepository.save(restaurant);
    }
}
