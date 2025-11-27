package com.example.restaurant.mapper.visitor;

import com.example.restaurant.dto.visitor.VisitorRequestDTO;
import com.example.restaurant.dto.visitor.VisitorResponseDTO;
import com.example.restaurant.entity.Visitor;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitorMapper {
    Visitor toEntity(VisitorRequestDTO dto);
    VisitorResponseDTO toResponseDto(Visitor visitor);
    List<VisitorResponseDTO> toResponseDtoList(List<Visitor> visitors);
}
