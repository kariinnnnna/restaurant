package com.example.restaurant.repository;

import com.example.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByUserRatingGreaterThanEqual(BigDecimal minRating);

    @Query("select r from Restaurant r where r.userRating >= :minRating")
    List<Restaurant> findWithRatingAtLeast(BigDecimal minRating);
}
