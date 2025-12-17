package com.example.restaurant.service;

import com.example.restaurant.dto.restaurant.RestaurantRequestDTO;
import com.example.restaurant.dto.restaurant.RestaurantResponseDTO;
import com.example.restaurant.entity.CuisineType;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.mapper.restaurant.RestaurantMapper;
import com.example.restaurant.repository.RestaurantRepository;
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
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void create_shouldSetUserRatingZeroAndSave() {
        RestaurantRequestDTO req = new RestaurantRequestDTO(
                "Place", "desc", CuisineType.EUROPEAN, BigDecimal.valueOf(1000)
        );

        Restaurant entity = Restaurant.builder()
                .name("Place")
                .description("desc")
                .cuisineType(CuisineType.EUROPEAN)
                .averageCheck(BigDecimal.valueOf(1000))
                .userRating(null)
                .build();

        Restaurant saved = Restaurant.builder()
                .id(1L)
                .name("Place")
                .description("desc")
                .cuisineType(CuisineType.EUROPEAN)
                .averageCheck(BigDecimal.valueOf(1000))
                .userRating(BigDecimal.ZERO)
                .build();

        RestaurantResponseDTO resp = new RestaurantResponseDTO(
                1L, "Place", "desc", CuisineType.EUROPEAN, BigDecimal.valueOf(1000), BigDecimal.ZERO
        );

        when(restaurantMapper.toEntity(req)).thenReturn(entity);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(saved);
        when(restaurantMapper.toResponseDto(saved)).thenReturn(resp);

        RestaurantResponseDTO result = restaurantService.create(req);

        assertEquals(BigDecimal.ZERO, result.userRating());
        ArgumentCaptor<Restaurant> captor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantRepository).save(captor.capture());
        assertEquals(BigDecimal.ZERO, captor.getValue().getUserRating());
    }

    @Test
    void findById_shouldReturnNullIfNotFound() {
        when(restaurantRepository.findById(77L)).thenReturn(Optional.empty());
        assertNull(restaurantService.findById(77L));
    }

    @Test
    void findByMinRating_shouldUseRepositoryMethod() {
        when(restaurantRepository.findByUserRatingGreaterThanEqual(BigDecimal.valueOf(4)))
                .thenReturn(List.of());

        List<RestaurantResponseDTO> result = restaurantService.findByMinRating(BigDecimal.valueOf(4));

        assertNotNull(result);
        verify(restaurantRepository).findByUserRatingGreaterThanEqual(BigDecimal.valueOf(4));
    }
}
