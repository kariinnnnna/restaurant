package com.example.restaurant.controller;

import com.example.restaurant.dto.rating.RatingRequestDTO;
import com.example.restaurant.dto.rating.RatingResponseDTO;
import com.example.restaurant.service.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RatingService ratingService;

    @Test
    void getAll_shouldReturn200() throws Exception {
        Mockito.when(ratingService.findAll())
                .thenReturn(List.of(new RatingResponseDTO(1L, 10L, 5, "ok")));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorId").value(1))
                .andExpect(jsonPath("$[0].restaurantId").value(10))
                .andExpect(jsonPath("$[0].score").value(5));
    }

    @Test
    void getById_shouldReturn200_whenFound() throws Exception {
        Mockito.when(ratingService.findById(1L, 10L))
                .thenReturn(new RatingResponseDTO(1L, 10L, 5, "ok"));

        mockMvc.perform(get("/api/reviews/1/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitorId").value(1))
                .andExpect(jsonPath("$.restaurantId").value(10));
    }

    @Test
    void getById_shouldReturn404_whenNotFound() throws Exception {
        Mockito.when(ratingService.findById(1L, 10L)).thenReturn(null);

        mockMvc.perform(get("/api/reviews/1/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201() throws Exception {
        RatingRequestDTO req = new RatingRequestDTO(1L, 10L, 5, "ok");

        Mockito.when(ratingService.create(any()))
                .thenReturn(new RatingResponseDTO(1L, 10L, 5, "ok"));

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/reviews/1/10"))
                .andExpect(jsonPath("$.score").value(5));
    }

    @Test
    void update_shouldReturn200_whenFound() throws Exception {
        RatingRequestDTO req = new RatingRequestDTO(1L, 10L, 4, "upd");

        Mockito.when(ratingService.update(eq(1L), eq(10L), any()))
                .thenReturn(new RatingResponseDTO(1L, 10L, 4, "upd"));

        mockMvc.perform(put("/api/reviews/1/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(4));
    }

    @Test
    void update_shouldReturn404_whenNotFound() throws Exception {
        RatingRequestDTO req = new RatingRequestDTO(1L, 10L, 4, "upd");

        Mockito.when(ratingService.update(eq(1L), eq(10L), any()))
                .thenReturn(null);

        mockMvc.perform(put("/api/reviews/1/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/reviews/1/10"))
                .andExpect(status().isNoContent());

        Mockito.verify(ratingService).delete(1L, 10L);
    }

    @Test
    void getPage_shouldReturn200() throws Exception {
        Page<RatingResponseDTO> page = new PageImpl<>(
                List.of(new RatingResponseDTO(1L, 10L, 5, "ok")),
                PageRequest.of(0, 10, Sort.by("score").ascending()),
                1
        );

        Mockito.when(ratingService.findPage(0, 10, true)).thenReturn(page);

        mockMvc.perform(get("/api/reviews/page?page=0&size=10&asc=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].score").value(5))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
    @Test
    void create_shouldReturn400_whenValidationFails() throws Exception {
        RatingRequestDTO badReq = new RatingRequestDTO(1L, 10L, 0, "bad");

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badReq)))
                .andExpect(status().isBadRequest());
    }

}
