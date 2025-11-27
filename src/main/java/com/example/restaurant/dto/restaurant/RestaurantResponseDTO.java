package com.example.restaurant.dto.restaurant;

import com.example.restaurant.entity.CuisineType;
import java.math.BigDecimal;

public record RestaurantResponseDTO(
        Long id,
        String name,
        String description,
        CuisineType cuisineType,
        BigDecimal averageCheck,
        BigDecimal userRating
) {}
