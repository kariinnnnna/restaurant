package com.example.restaurant.service;


import com.example.restaurant.dto.rating.RatingRequestDTO;
import com.example.restaurant.dto.rating.RatingResponseDTO;
import com.example.restaurant.entity.*;
import com.example.restaurant.mapper.rating.RatingMapper;
import com.example.restaurant.repository.RatingRepository;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final VisitorRepository visitorRepository;
    private final RatingMapper ratingMapper;

    public RatingResponseDTO create(RatingRequestDTO dto) {
        Visitor visitor = visitorRepository.findById(dto.visitorId())
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found: " + dto.visitorId()));

        Restaurant restaurant = restaurantRepository.findById(dto.restaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + dto.restaurantId()));

        RatingId id = new RatingId(visitor.getId(), restaurant.getId());

        Rating rating = Rating.builder()
                .id(id)
                .visitor(visitor)
                .restaurant(restaurant)
                .score(dto.score())
                .reviewText(dto.reviewText())
                .build();

        rating = ratingRepository.save(rating);

        recalculateRestaurantRating(restaurant.getId());

        return ratingMapper.toResponseDto(rating);
    }

    public List<RatingResponseDTO> findAll() {
        return ratingRepository.findAll()
                .stream()
                .map(ratingMapper::toResponseDto)
                .toList();
    }

    public RatingResponseDTO findById(Long visitorId, Long restaurantId) {
        RatingId id = new RatingId(visitorId, restaurantId);
        return ratingRepository.findById(id)
                .map(ratingMapper::toResponseDto)
                .orElse(null);
    }


    public RatingResponseDTO update(Long visitorId, Long restaurantId, RatingRequestDTO dto) {
        RatingId id = new RatingId(visitorId, restaurantId);

        return ratingRepository.findById(id)
                .map(existing -> {
                    Visitor visitor = visitorRepository.findById(dto.visitorId())
                            .orElseThrow(() -> new IllegalArgumentException("Visitor not found: " + dto.visitorId()));
                    Restaurant restaurant = restaurantRepository.findById(dto.restaurantId())
                            .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + dto.restaurantId()));

                    Long oldRestaurantId = existing.getRestaurant().getId();

                    existing.setId(new RatingId(visitor.getId(), restaurant.getId()));
                    existing.setVisitor(visitor);
                    existing.setRestaurant(restaurant);
                    existing.setScore(dto.score());
                    existing.setReviewText(dto.reviewText());

                    Rating saved = ratingRepository.save(existing);

                    recalculateRestaurantRating(oldRestaurantId);
                    recalculateRestaurantRating(saved.getRestaurant().getId());

                    return ratingMapper.toResponseDto(saved);
                })
                .orElse(null);
    }

    public void delete(Long visitorId, Long restaurantId) {
        RatingId id = new RatingId(visitorId, restaurantId);
        ratingRepository.findById(id).ifPresent(existing -> {
            Long restaurantIdToUpdate = existing.getRestaurant().getId();
            ratingRepository.deleteById(id);
            recalculateRestaurantRating(restaurantIdToUpdate);
        });
    }

    public Page<RatingResponseDTO> findPage(int page, int size, boolean asc) {
        Sort sort = Sort.by("score");
        if (!asc) {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);

        return ratingRepository.findAll(pageable)
                .map(ratingMapper::toResponseDto);
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            return;
        }

        List<Rating> restaurantRatings = ratingRepository.findByRestaurant_Id(restaurantId);

        if (restaurantRatings.isEmpty()) {
            restaurant.setUserRating(BigDecimal.ZERO);
        } else {
            BigDecimal sum = restaurantRatings.stream()
                    .map(r -> BigDecimal.valueOf(r.getScore()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal average = sum.divide(
                    BigDecimal.valueOf(restaurantRatings.size()),
                    2,
                    RoundingMode.HALF_UP
            );

            restaurant.setUserRating(average);
        }

        restaurantRepository.save(restaurant);
    }
}
