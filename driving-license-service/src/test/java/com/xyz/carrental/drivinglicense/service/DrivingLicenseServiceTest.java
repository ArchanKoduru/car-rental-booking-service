package com.xyz.carrental.drivinglicense.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.xyz.carrental.drivinglicense.controller.LicenseController;
import com.xyz.carrental.drivinglicense.entity.DrivingLicense;
import com.xyz.carrental.drivinglicense.exception.LicenseNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LicenseController.class)
class LicenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DrivingLicenseService service;

    @Test
    void shouldReturnLicenseForValidNumber() throws Exception {
        var license = new DrivingLicense("DL123", "John Doe", LocalDate.of(2025, 12, 31));

        when(service.getLicense("DL123")).thenReturn(license);

        mockMvc.perform(get("/license/details/DL123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerName").value("John Doe"))
                .andExpect(jsonPath("$.expiryDate").value("2025-12-31"));
    }

    @Test
    void shouldReturn404ForInvalidLicense() throws Exception {
        when(service.getLicense("INVALID")).thenThrow(new LicenseNotFoundException("Driving license not found: INVALID"));

        mockMvc.perform(get("/license/details/INVALID"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Driving license not found: INVALID"));
    }
}

