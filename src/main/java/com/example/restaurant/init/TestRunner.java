package com.example.restaurant.init;

import com.example.restaurant.entity.Rating;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.entity.Visitor;
import com.example.restaurant.service.RatingService;
import com.example.restaurant.service.RestaurantService;
import com.example.restaurant.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {

    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final RatingService ratingService;

    @Override
    public void run(String... args) {
        System.out.println("1. Посетители");
        List<Visitor> visitors = visitorService.findAll();
        visitors.forEach(System.out::println);

        System.out.println("\n2. Рестораны");
        List<Restaurant> restaurants = restaurantService.findAll();
        restaurants.forEach(System.out::println);

        System.out.println("\n3. Оценки");
        List<Rating> ratings = ratingService.findAll();
        ratings.forEach(System.out::println);

        System.out.println("\n4. Считаем рейтинг");
        restaurants.forEach(r ->
                System.out.printf("Ресторан: %s, средняя оценка: %s%n",
                        r.getName(), r.getUserRating())
        );
    }
}
