package com.example.restaurant.repository;

import com.example.restaurant.entity.Visitor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitorRepository {

    private final List<Visitor> visitors = new ArrayList<>();

    private long idCounter = 1;

    public Visitor save(Visitor visitor) {
        if (visitor.getId() == null) {
            visitor.setId(idCounter++);
        } else {
            visitors.removeIf(v -> v.getId().equals(visitor.getId()));
        }
        visitors.add(visitor);
        return visitor;
    }
    public void remove(Long id) {
        visitors.removeIf(v -> v.getId().equals(id));
    }
    public List<Visitor> findAll() {
        return new ArrayList<>(visitors);
    }
    public Visitor findById(Long id) {
        return visitors.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
