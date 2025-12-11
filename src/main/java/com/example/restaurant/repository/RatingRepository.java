package com.example.restaurant.repository;

import com.example.restaurant.entity.Rating;
import com.example.restaurant.entity.RatingId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    Page<Rating> findAll(Pageable pageable);

    List<Rating> findByRestaurant_Id(Long restaurantId);
}
