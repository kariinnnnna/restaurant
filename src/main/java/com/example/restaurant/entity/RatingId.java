package com.example.restaurant.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingId implements Serializable {

    private Long visitorId;
    private Long restaurantId;
}
