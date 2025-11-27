package com.example.restaurant.dto.visitor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VisitorRequestDTO(
        String name,
        @NotNull @Min(0) Integer age,
        @NotNull String gender
) {}
