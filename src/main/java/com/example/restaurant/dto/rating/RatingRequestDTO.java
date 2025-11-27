package com.example.restaurant.dto.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RatingRequestDTO(
        @NotNull Long visitorId,
        @NotNull Long restaurantId,
        @NotNull @Min(1) @Max(5) Integer score,
        String reviewText
) {}
