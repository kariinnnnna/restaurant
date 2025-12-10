package com.example.restaurant.dto.restaurant;

import com.example.restaurant.entity.CuisineType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RestaurantRequestDTO(
        @NotNull String name,
        String description,
        @NotNull CuisineType cuisineType,
        @NotNull BigDecimal averageCheck
) {}
