package com.xyz.carrental.drivinglicense.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyz.carrental.drivinglicense.entity.DrivingLicense;
import com.xyz.carrental.drivinglicense.exception.LicenseNotFoundException;
import com.xyz.carrental.drivinglicense.service.DrivingLicenseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
class LicenseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DrivingLicenseService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnLicenseDetails() throws Exception {
        // Mock service response
        DrivingLicense license = new DrivingLicense("DL123456789", "John Doe", LocalDate.of(2025, 12, 31));
        when(service.getLicense("DL123456789")).thenReturn(license);

        mockMvc.perform(get("/license/details/{licenseNumber}", "DL123456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerName").value("John Doe"))
                .andExpect(jsonPath("$.expiryDate").value("2025-12-31"));
    }

    @Test
    void shouldReturn404ForInvalidLicense() throws Exception {
        // Mock service exception
        when(service.getLicense("DL999999999")).thenThrow(new LicenseNotFoundException("Driving license not found"));

        mockMvc.perform(get("/license/details/{licenseNumber}", "DL999999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Driving license not found"));
    }
}
