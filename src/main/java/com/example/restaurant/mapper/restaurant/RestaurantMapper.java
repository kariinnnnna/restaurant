package com.example.restaurant.mapper.restaurant;

import com.example.restaurant.dto.restaurant.RestaurantRequestDTO;
import com.example.restaurant.dto.restaurant.RestaurantResponseDTO;
import com.example.restaurant.entity.Restaurant;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    Restaurant toEntity(RestaurantRequestDTO dto);
    RestaurantResponseDTO toResponseDto(Restaurant restaurant);
    List<RestaurantResponseDTO> toResponseDtoList(List<Restaurant> restaurants);
}
