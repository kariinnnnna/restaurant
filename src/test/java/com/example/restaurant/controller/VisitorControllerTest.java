package com.example.restaurant.controller;

import com.example.restaurant.dto.visitor.VisitorRequestDTO;
import com.example.restaurant.dto.visitor.VisitorResponseDTO;
import com.example.restaurant.service.VisitorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitorController.class)
class VisitorControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean
    VisitorService visitorService;

    @Test
    void getAll_shouldReturn200() throws Exception {
        Mockito.when(visitorService.findAll())
                .thenReturn(List.of(new VisitorResponseDTO(1L, "A", 10, "M")));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void create_shouldReturn201() throws Exception {
        VisitorRequestDTO req = new VisitorRequestDTO("A", 10, "M");
        Mockito.when(visitorService.create(any()))
                .thenReturn(new VisitorResponseDTO(1L, "A", 10, "M"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}
