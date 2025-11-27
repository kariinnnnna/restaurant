package com.example.restaurant.dto.visitor;

public record VisitorResponseDTO(
        Long id,
        String name,
        Integer age,
        String gender
) {}
