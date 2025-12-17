package com.example.restaurant.service;

import com.example.restaurant.dto.visitor.VisitorRequestDTO;
import com.example.restaurant.dto.visitor.VisitorResponseDTO;
import com.example.restaurant.entity.Visitor;
import com.example.restaurant.mapper.visitor.VisitorMapper;
import com.example.restaurant.repository.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorServiceTest {

    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private VisitorMapper visitorMapper;

    @InjectMocks
    private VisitorService visitorService;

    @Test
    void create_shouldSaveAndReturnDto() {
        VisitorRequestDTO req = new VisitorRequestDTO("Alex", 20, "M");

        Visitor entity = Visitor.builder().name("Alex").age(20).gender("M").build();
        Visitor saved = Visitor.builder().id(1L).name("Alex").age(20).gender("M").build();

        VisitorResponseDTO resp = new VisitorResponseDTO(1L, "Alex", 20, "M");

        when(visitorMapper.toEntity(req)).thenReturn(entity);
        when(visitorRepository.save(entity)).thenReturn(saved);
        when(visitorMapper.toResponseDto(saved)).thenReturn(resp);

        VisitorResponseDTO result = visitorService.create(req);

        assertEquals(1L, result.id());
        verify(visitorRepository).save(entity);
    }

    @Test
    void findById_shouldReturnNullIfNotFound() {
        when(visitorRepository.findById(99L)).thenReturn(Optional.empty());

        VisitorResponseDTO result = visitorService.findById(99L);

        assertNull(result);
    }

    @Test
    void findAll_shouldReturnMappedList() {
        List<Visitor> entities = List.of(
                Visitor.builder().id(1L).name("A").age(10).gender("M").build(),
                Visitor.builder().id(2L).name(null).age(20).gender("F").build()
        );

        when(visitorRepository.findAll()).thenReturn(entities);
        when(visitorMapper.toResponseDto(any())).thenAnswer(inv -> {
            Visitor v = inv.getArgument(0);
            return new VisitorResponseDTO(v.getId(), v.getName(), v.getAge(), v.getGender());
        });

        List<VisitorResponseDTO> result = visitorService.findAll();

        assertEquals(2, result.size());
        verify(visitorRepository).findAll();
    }

    @Test
    void delete_shouldCallRepository() {
        visitorService.delete(5L);
        verify(visitorRepository).deleteById(5L);
    }
}
