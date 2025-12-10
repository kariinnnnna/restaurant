package com.example.restaurant.controller;

import com.example.restaurant.dto.rating.RatingRequestDTO;
import com.example.restaurant.dto.rating.RatingResponseDTO;
import com.example.restaurant.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // создать: POST /api/reviews
    @PostMapping
    public ResponseEntity<RatingResponseDTO> create(@Valid @RequestBody RatingRequestDTO dto) {
        RatingResponseDTO created = ratingService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/reviews/" + created.id()))
                .body(created);
    }

    // вывод всех: GET /api/reviews
    @GetMapping
    public List<RatingResponseDTO> getAll() {
        return ratingService.findAll();
    }

    // вывод одного: GET /api/reviews/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RatingResponseDTO> getById(@PathVariable Long id) {
        RatingResponseDTO rating = ratingService.findById(id);
        if (rating == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rating);
    }

    // обновить: PUT /api/reviews/{id}
    @PutMapping("/{id}")
    public ResponseEntity<RatingResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RatingRequestDTO dto
    ) {
        RatingResponseDTO updated = ratingService.update(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // удалить: DELETE /api/reviews/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ratingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
