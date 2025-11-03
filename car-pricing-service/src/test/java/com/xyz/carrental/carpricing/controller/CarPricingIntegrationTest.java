package com.xyz.carrental.carpricing.controller;

import com.xyz.carrental.carpricing.entity.CarPricing;
import com.xyz.carrental.carpricing.repository.CarPricingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CarPricingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarPricingRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        repository.save(new CarPricing("MEDIUM", BigDecimal.valueOf(50.0)));
    }

    @Test
    void shouldReturnRateForValidCategory() throws Exception {
        mockMvc.perform(get("/rental/rate/MEDIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("MEDIUM"))
                .andExpect(jsonPath("$.ratePerDay").value(50.0));
    }

    @Test
    void shouldReturn404ForInvalidCategory() throws Exception {
        mockMvc.perform(get("/rental/rate/SMALL"))
                .andExpect(status().isNotFound());
    }
}
