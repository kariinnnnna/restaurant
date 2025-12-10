package com.example.restaurant.service;

import com.example.restaurant.dto.rating.RatingRequestDTO;
import com.example.restaurant.dto.rating.RatingResponseDTO;
import com.example.restaurant.entity.Rating;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.mapper.rating.RatingMapper;
import com.example.restaurant.repository.RatingRepository;
import com.example.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final RatingMapper ratingMapper;

    public RatingResponseDTO create(RatingRequestDTO dto) {
        Rating rating = ratingMapper.toEntity(dto);
        ratingRepository.save(rating);
        recalculateRestaurantRating(rating.getRestaurantId());
        return ratingMapper.toResponseDto(rating);
    }

    public List<RatingResponseDTO> findAll() {
        return ratingMapper.toResponseDtoList(ratingRepository.findAll());
    }

    public RatingResponseDTO findById(Long id) {
        Rating rating = ratingRepository.findById(id);
        return rating != null ? ratingMapper.toResponseDto(rating) : null;
    }

    public RatingResponseDTO update(Long id, RatingRequestDTO dto) {
        Rating existing = ratingRepository.findById(id);
        if (existing == null) {
            return null;
        }

        Long oldRestaurantId = existing.getRestaurantId();

        existing.setVisitorId(dto.visitorId());
        existing.setRestaurantId(dto.restaurantId());
        existing.setScore(dto.score());
        existing.setReviewText(dto.reviewText());

        ratingRepository.save(existing);

        recalculateRestaurantRating(oldRestaurantId);
        if (!oldRestaurantId.equals(existing.getRestaurantId())) {
            recalculateRestaurantRating(existing.getRestaurantId());
        }

        return ratingMapper.toResponseDto(existing);
    }

    public void delete(Long id) {
        Rating existing = ratingRepository.findById(id);
        if (existing != null) {
            Long restaurantId = existing.getRestaurantId();
            ratingRepository.remove(id);
            recalculateRestaurantRating(restaurantId);
        }
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            return;
        }

        List<Rating> allRatings = ratingRepository.findAll();
        List<Rating> restaurantRatings = allRatings.stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .toList();

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
