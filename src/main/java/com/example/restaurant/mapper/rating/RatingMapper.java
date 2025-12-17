package com.example.restaurant.mapper.rating;

import com.example.restaurant.dto.rating.RatingResponseDTO;
import com.example.restaurant.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mapping(target = "visitorId", source = "visitor.id")
    @Mapping(target = "restaurantId", source = "restaurant.id")
    RatingResponseDTO toResponseDto(Rating rating);

    List<RatingResponseDTO> toResponseDtoList(List<Rating> ratings);
}
