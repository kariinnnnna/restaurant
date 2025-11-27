package com.example.restaurant.service;

import com.example.restaurant.dto.restaurant.RestaurantRequestDTO;
import com.example.restaurant.dto.restaurant.RestaurantResponseDTO;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.mapper.restaurant.RestaurantMapper;
import com.example.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantResponseDTO create(RestaurantRequestDTO dto) {
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        restaurantRepository.save(restaurant);
        return restaurantMapper.toResponseDto(restaurant);
    }

    public List<RestaurantResponseDTO> findAll() {
        return restaurantMapper.toResponseDtoList(restaurantRepository.findAll());
    }

    public RestaurantResponseDTO findById(Long id) {
        Restaurant r = restaurantRepository.findById(id);
        return r != null ? restaurantMapper.toResponseDto(r) : null;
    }

    public RestaurantResponseDTO update(Long id, RestaurantRequestDTO dto) {
        Restaurant existing = restaurantRepository.findById(id);
        if (existing == null) {
            return null;
        }

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setCuisineType(dto.cuisineType());
        existing.setAverageCheck(dto.averageCheck());

        restaurantRepository.save(existing);
        return restaurantMapper.toResponseDto(existing);
    }

    public void delete(Long id) {
        restaurantRepository.remove(id);
    }
}
