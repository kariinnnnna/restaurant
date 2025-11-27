package com.example.restaurant.repository;

import com.example.restaurant.entity.Rating;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RatingRepository {

    private final List<Rating> ratings = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    public Rating save(Rating rating) {
        if (rating.getId() == null) {
            rating.setId(idGenerator.getAndIncrement());
        } else {
            ratings.removeIf(r -> r.getId().equals(rating.getId()));
        }
        ratings.add(rating);
        return rating;
    }

    public void remove(Long id) {
        ratings.removeIf(r -> r.getId().equals(id));
    }

    public List<Rating> findAll() {
        return new ArrayList<>(ratings);
    }

    public Rating findById(Long id) {
        return ratings.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
