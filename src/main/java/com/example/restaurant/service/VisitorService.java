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
        visitorRepository.save(visitor);
        return visitorMapper.toResponseDto(visitor);
    }

    public List<VisitorResponseDTO> findAll() {
        return visitorMapper.toResponseDtoList(visitorRepository.findAll());
    }

    public VisitorResponseDTO findById(Long id) {
        Visitor v = visitorRepository.findById(id);
        return v != null ? visitorMapper.toResponseDto(v) : null;
    }

    public VisitorResponseDTO update(Long id, VisitorRequestDTO dto) {
        Visitor existing = visitorRepository.findById(id);
        if (existing == null) {
            return null;
        }

        existing.setName(dto.name());
        existing.setAge(dto.age());
        existing.setGender(dto.gender());

        visitorRepository.save(existing);
        return visitorMapper.toResponseDto(existing);
    }

    public void delete(Long id) {
        visitorRepository.remove(id);
    }
}
