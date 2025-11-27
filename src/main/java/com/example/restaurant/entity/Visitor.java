package com.example.restaurant.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visitor {
    @NotNull
    private Long id;

    private String name;

    @NotNull
    @Min(0)
    private Integer age;

    @NotNull
    private String gender;
}
