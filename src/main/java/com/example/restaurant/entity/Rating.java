package com.example.restaurant.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @NotNull
    private Long id;

    @NotNull
    private Long visitorId;

    @NotNull
    private Long restaurantId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;

    private String reviewText;
}
