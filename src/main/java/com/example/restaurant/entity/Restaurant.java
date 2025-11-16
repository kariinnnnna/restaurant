package com.example.restaurant.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @NotNull
    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private CuisineType cuisineType;

    @NotNull
    private BigDecimal averageCheck;

    @Builder.Default
    private BigDecimal userRating = BigDecimal.ZERO;
}
