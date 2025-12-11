package com.example.restaurant.service;

import com.example.restaurant.dto.visitor.VisitorRequestDTO;
import com.example.restaurant.dto.visitor.VisitorResponseDTO;
import com.example.restaurant.entity.Visitor;
import com.example.restaurant.mapper.visitor.VisitorMapper;
import com.example.restaurant.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final VisitorMapper visitorMapper;

    public VisitorResponseDTO create(VisitorRequestDTO dto) {
        Visitor visitor = visitorMapper.toEntity(dto);
        visitor = visitorRepository.save(visitor);
        return visitorMapper.toResponseDto(visitor);
    }

    public List<VisitorResponseDTO> findAll() {
        return visitorRepository.findAll()
                .stream()
                .map(visitorMapper::toResponseDto)
                .toList();
    }

    public VisitorResponseDTO findById(Long id) {
        return visitorRepository.findById(id)
                .map(visitorMapper::toResponseDto)
                .orElse(null);
    }

    public VisitorResponseDTO update(Long id, VisitorRequestDTO dto) {
        return visitorRepository.findById(id)
                .map(existing -> {
                    existing.setName(dto.name());
                    existing.setAge(dto.age());
                    existing.setGender(dto.gender());
                    Visitor saved = visitorRepository.save(existing);
                    return visitorMapper.toResponseDto(saved);
                })
                .orElse(null);
    }

    public void delete(Long id) {
        visitorRepository.deleteById(id);
    }
}
