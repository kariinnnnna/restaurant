package com.example.restaurant.controller;

import com.example.restaurant.dto.visitor.VisitorRequestDTO;
import com.example.restaurant.dto.visitor.VisitorResponseDTO;
import com.example.restaurant.service.VisitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    // создать: POST /api/users
    @PostMapping
    public ResponseEntity<VisitorResponseDTO> create(@Valid @RequestBody VisitorRequestDTO dto) {
        VisitorResponseDTO created = visitorService.create(dto);
        // можно вернуть Location: /api/users/{id}
        return ResponseEntity
                .created(URI.create("/api/users/" + created.id()))
                .body(created);
    }

    // вывод всех: GET /api/users
    @GetMapping
    public List<VisitorResponseDTO> getAll() {
        return visitorService.findAll();
    }

    // вывод одного: GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<VisitorResponseDTO> getById(@PathVariable Long id) {
        VisitorResponseDTO visitor = visitorService.findById(id);
        if (visitor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(visitor);
    }

    // обновить: PUT /api/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<VisitorResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody VisitorRequestDTO dto
    ) {
        VisitorResponseDTO updated = visitorService.update(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // удалить: DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        visitorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
