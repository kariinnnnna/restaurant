package com.example.restaurant.repository;

import com.example.restaurant.entity.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantRepository {

    private final List<Restaurant> restaurants = new ArrayList<>();

    public Restaurant save(Restaurant restaurant) {
        restaurants.removeIf(r -> r.getId().equals(restaurant.getId()));
        restaurants.add(restaurant);
        return restaurant;
    }

    public void remove(Long id) {
        restaurants.removeIf(r -> r.getId().equals(id));
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants);
    }

    public Restaurant findById(Long id) {
        return restaurants.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
