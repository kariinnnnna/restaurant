package com.example.restaurant.service;

import com.example.restaurant.dto.restaurant.RestaurantRequestDTO;
import com.example.restaurant.dto.restaurant.RestaurantResponseDTO;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.mapper.restaurant.RestaurantMapper;
import com.example.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantResponseDTO create(RestaurantRequestDTO dto) {
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        if (restaurant.getUserRating() == null) {
            restaurant.setUserRating(BigDecimal.ZERO);
        }
        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponseDto(restaurant);
    }

    public List<RestaurantResponseDTO> findAll() {
        return restaurantRepository.findAll()
                .stream()
                .map(restaurantMapper::toResponseDto)
                .toList();
    }

    public RestaurantResponseDTO findById(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurantMapper::toResponseDto)
                .orElse(null);
    }

    public RestaurantResponseDTO update(Long id, RestaurantRequestDTO dto) {
        return restaurantRepository.findById(id)
                .map(existing -> {
                    existing.setName(dto.name());
                    existing.setDescription(dto.description());
                    existing.setCuisineType(dto.cuisineType());
                    existing.setAverageCheck(dto.averageCheck());
                    Restaurant saved = restaurantRepository.save(existing);
                    return restaurantMapper.toResponseDto(saved);
                })
                .orElse(null);
    }

    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }

    public List<RestaurantResponseDTO> findByMinRating(BigDecimal minRating) {
        return restaurantRepository.findByUserRatingGreaterThanEqual(minRating)
                .stream()
                .map(restaurantMapper::toResponseDto)
                .toList();
    }

    public List<RestaurantResponseDTO> findByMinRatingQuery(BigDecimal minRating) {
        return restaurantRepository.findWithRatingAtLeast(minRating)
                .stream()
                .map(restaurantMapper::toResponseDto)
                .toList();
    }
}
