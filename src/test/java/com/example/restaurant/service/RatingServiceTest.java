package com.example.restaurant.service;

import com.example.restaurant.dto.rating.RatingRequestDTO;
import com.example.restaurant.dto.rating.RatingResponseDTO;
import com.example.restaurant.entity.*;
import com.example.restaurant.mapper.rating.RatingMapper;
import com.example.restaurant.repository.RatingRepository;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.repository.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock private RatingRepository ratingRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private VisitorRepository visitorRepository;
    @Mock private RatingMapper ratingMapper;

    @InjectMocks private RatingService ratingService;

    @Test
    void create_shouldSaveRatingAndRecalculateRestaurantRating() {
        RatingRequestDTO req = new RatingRequestDTO(1L, 10L, 5, "ok");

        Visitor v = Visitor.builder().id(1L).age(20).gender("M").name("A").build();
        Restaurant r = Restaurant.builder().id(10L).name("R").cuisineType(CuisineType.EUROPEAN)
                .averageCheck(BigDecimal.valueOf(1000)).userRating(BigDecimal.ZERO).build();

        Rating saved = Rating.builder()
                .id(new RatingId(1L, 10L))
                .visitor(v)
                .restaurant(r)
                .score(5)
                .reviewText("ok")
                .build();

        when(visitorRepository.findById(1L)).thenReturn(Optional.of(v));
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(r));
        when(ratingRepository.save(any(Rating.class))).thenReturn(saved);

        when(ratingRepository.findByRestaurant_Id(10L)).thenReturn(List.of(saved));

        RatingResponseDTO resp = new RatingResponseDTO(1L, 10L, 5, "ok");
        when(ratingMapper.toResponseDto(saved)).thenReturn(resp);

        RatingResponseDTO result = ratingService.create(req);

        assertEquals(5, result.score());
        verify(ratingRepository).save(any(Rating.class));
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void findById_shouldReturnNullIfNotFound() {
        when(ratingRepository.findById(new RatingId(1L, 2L))).thenReturn(Optional.empty());
        assertNull(ratingService.findById(1L, 2L));
    }

    @Test
    void delete_shouldDeleteAndRecalculate() {
        Visitor v = Visitor.builder().id(1L).age(20).gender("M").build();
        Restaurant r = Restaurant.builder().id(10L).name("R").cuisineType(CuisineType.EUROPEAN)
                .averageCheck(BigDecimal.valueOf(1000)).userRating(BigDecimal.ZERO).build();

        Rating existing = Rating.builder()
                .id(new RatingId(1L, 10L))
                .visitor(v)
                .restaurant(r)
                .score(3)
                .build();

        when(ratingRepository.findById(new RatingId(1L, 10L))).thenReturn(Optional.of(existing));
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(r));
        when(ratingRepository.findByRestaurant_Id(10L)).thenReturn(List.of());

        ratingService.delete(1L, 10L);

        verify(ratingRepository).deleteById(new RatingId(1L, 10L));
        verify(restaurantRepository).save(any(Restaurant.class));
    }
}
