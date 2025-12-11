package com.example.restaurant.dto.rating;

public record RatingResponseDTO(
        Long visitorId,
        Long restaurantId,
        Integer score,
        String reviewText
) {}
