package com.example.restaurant.controller;

import com.example.restaurant.dto.rating.RatingRequestDTO;
import com.example.restaurant.dto.rating.RatingResponseDTO;
import com.example.restaurant.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // создать
    @PostMapping
    public ResponseEntity<RatingResponseDTO> create(@Valid @RequestBody RatingRequestDTO dto) {
        RatingResponseDTO created = ratingService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/reviews/"
                        + created.visitorId() + "/" + created.restaurantId()))
                .body(created);
    }

    // вывести всех
    @GetMapping
    public List<RatingResponseDTO> getAll() {
        return ratingService.findAll();
    }

    // вывести одного
    @GetMapping("/{visitorId}/{restaurantId}")
    public ResponseEntity<RatingResponseDTO> getById(
            @PathVariable Long visitorId,
            @PathVariable Long restaurantId
    ) {
        RatingResponseDTO rating = ratingService.findById(visitorId, restaurantId);
        return rating != null ? ResponseEntity.ok(rating) : ResponseEntity.notFound().build();
    }

    // обновить
    @PutMapping("/{visitorId}/{restaurantId}")
    public ResponseEntity<RatingResponseDTO> update(
            @PathVariable Long visitorId,
            @PathVariable Long restaurantId,
            @Valid @RequestBody RatingRequestDTO dto
    ) {
        RatingResponseDTO updated = ratingService.update(visitorId, restaurantId, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // удалить
    @DeleteMapping("/{visitorId}/{restaurantId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long visitorId,
            @PathVariable Long restaurantId
    ) {
        ratingService.delete(visitorId, restaurantId);
        return ResponseEntity.noContent().build();
    }

    // пагинация
    @GetMapping("/page")
    public Page<RatingResponseDTO> getPage(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "true") boolean asc
    ) {
        return ratingService.findPage(page, size, asc);
    }
}
