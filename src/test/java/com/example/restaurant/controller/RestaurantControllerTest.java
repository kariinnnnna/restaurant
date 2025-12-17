package com.example.restaurant.controller;

import com.example.restaurant.dto.restaurant.RestaurantRequestDTO;
import com.example.restaurant.dto.restaurant.RestaurantResponseDTO;
import com.example.restaurant.entity.CuisineType;
import com.example.restaurant.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantService restaurantService;

    @Test
    void getAll_shouldReturn200() throws Exception {
        Mockito.when(restaurantService.findAll())
                .thenReturn(List.of(
                        new RestaurantResponseDTO(
                                1L, "R", "D", CuisineType.EUROPEAN,
                                BigDecimal.valueOf(1000), BigDecimal.ZERO
                        )
                ));

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("R"));
    }

    @Test
    void getById_shouldReturn200_whenFound() throws Exception {
        Mockito.when(restaurantService.findById(1L))
                .thenReturn(new RestaurantResponseDTO(
                        1L, "R", "D", CuisineType.EUROPEAN,
                        BigDecimal.valueOf(1000), BigDecimal.ZERO
                ));

        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cuisineType").value("EUROPEAN"));
    }

    @Test
    void getById_shouldReturn404_whenNotFound() throws Exception {
        Mockito.when(restaurantService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/restaurants/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201() throws Exception {
        RestaurantRequestDTO req = new RestaurantRequestDTO(
                "New", "Desc", CuisineType.ITALIAN, BigDecimal.valueOf(1200)
        );

        Mockito.when(restaurantService.create(any()))
                .thenReturn(new RestaurantResponseDTO(
                        10L, "New", "Desc", CuisineType.ITALIAN,
                        BigDecimal.valueOf(1200), BigDecimal.ZERO
                ));

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/restaurants/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.userRating").value(0));
    }

    @Test
    void update_shouldReturn200_whenFound() throws Exception {
        RestaurantRequestDTO req = new RestaurantRequestDTO(
                "Upd", "D2", CuisineType.CHINESE, BigDecimal.valueOf(900)
        );

        Mockito.when(restaurantService.update(eq(1L), any()))
                .thenReturn(new RestaurantResponseDTO(
                        1L, "Upd", "D2", CuisineType.CHINESE,
                        BigDecimal.valueOf(900), BigDecimal.valueOf(4.50)
                ));

        mockMvc.perform(put("/api/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Upd"))
                .andExpect(jsonPath("$.userRating").value(4.50));
    }

    @Test
    void update_shouldReturn404_whenNotFound() throws Exception {
        RestaurantRequestDTO req = new RestaurantRequestDTO(
                "Upd", "D2", CuisineType.CHINESE, BigDecimal.valueOf(900)
        );

        Mockito.when(restaurantService.update(eq(99L), any())).thenReturn(null);

        mockMvc.perform(put("/api/restaurants/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(restaurantService).delete(1L);
    }
}
