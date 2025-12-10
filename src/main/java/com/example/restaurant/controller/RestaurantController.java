package com.example.restaurant.controller;

import com.example.restaurant.dto.restaurant.RestaurantRequestDTO;
import com.example.restaurant.dto.restaurant.RestaurantResponseDTO;
import com.example.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // создать: POST /api/restaurants
    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> create(@Valid @RequestBody RestaurantRequestDTO dto) {
        RestaurantResponseDTO created = restaurantService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/restaurants/" + created.id()))
                .body(created);
    }

    // вывод всех: GET /api/restaurants
    @GetMapping
    public List<RestaurantResponseDTO> getAll() {
        return restaurantService.findAll();
    }

    // вывод одного: GET /api/restaurants/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> getById(@PathVariable Long id) {
        RestaurantResponseDTO restaurant = restaurantService.findById(id);
        if (restaurant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(restaurant);
    }

    // обновить: PUT /api/restaurants/{id}
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequestDTO dto
    ) {
        RestaurantResponseDTO updated = restaurantService.update(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // удалить: DELETE /api/restaurants/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
