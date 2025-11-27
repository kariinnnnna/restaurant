package com.example.restaurant.mapper.rating;

import com.example.restaurant.dto.rating.RatingRequestDTO;
import com.example.restaurant.dto.rating.RatingResponseDTO;
import com.example.restaurant.entity.Rating;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    Rating toEntity(RatingRequestDTO dto);
    RatingResponseDTO toResponseDto(Rating rating);
    List<RatingResponseDTO> toResponseDtoList(List<Rating> ratings);
}
