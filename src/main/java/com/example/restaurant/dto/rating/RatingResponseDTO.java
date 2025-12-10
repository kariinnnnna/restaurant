package com.example.restaurant.dto.rating;

public record RatingResponseDTO(
        Long id,
        Long visitorId,
        Long restaurantId,
        Integer score,
        String reviewText
) {}
