package com.example.restaurant.init;

import com.example.restaurant.entity.*;
import com.example.restaurant.service.RatingService;
import com.example.restaurant.service.RestaurantService;
import com.example.restaurant.service.VisitorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final RatingService ratingService;

    @PostConstruct
    public void initData() {
        visitorService.save(Visitor.builder()
                .id(1L)
                .name("Борик")
                .age(25)
                .gender("М")
                .build());
        visitorService.save(Visitor.builder()
                .id(2L)
                .name(null)
                .age(30)
                .gender("Ж")
                .build());
        restaurantService.save(Restaurant.builder()
                .id(1L)
                .name("Сашимка")
                .description("Японская кухня")
                .cuisineType(CuisineType.JAPANESE)
                .averageCheck(BigDecimal.valueOf(1500))
                .userRating(BigDecimal.ZERO)
                .build());
        restaurantService.save(Restaurant.builder()
                .id(2L)
                .name("Дом карбонары")
                .description("Европейская кухня")
                .cuisineType(CuisineType.EUROPEAN)
                .averageCheck(BigDecimal.valueOf(1000))
                .userRating(BigDecimal.ZERO)
                .build());

        ratingService.save(Rating.builder()
                .id(null)
                .visitorId(1L)
                .restaurantId(1L)
                .score(5)
                .reviewText("Необычно!")
                .build());
        ratingService.save(Rating.builder()
                .id(null)
                .visitorId(2L)
                .restaurantId(2L)
                .score(3)
                .reviewText(null)
                .build());
        ratingService.save(Rating.builder()
                .id(null)
                .visitorId(1L)
                .restaurantId(2L)
                .score(1)
                .reviewText("Не обрадовали :( ")
                .build());
    }
}
