package com.example.restaurant.repository;

import com.example.restaurant.entity.Visitor;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitorRepository {

    private final List<Visitor> visitors = new ArrayList<>();

    public Visitor save(Visitor visitor) {
        visitors.removeIf(v -> v.getId().equals(visitor.getId()));
        visitors.add(visitor);
        return visitor;
    }

    public void remove(Long id) {
        visitors.removeIf(v -> v.getId().equals(id));
    }

    public List<Visitor> findAll() {
        return new ArrayList<>(visitors);
    }
}
