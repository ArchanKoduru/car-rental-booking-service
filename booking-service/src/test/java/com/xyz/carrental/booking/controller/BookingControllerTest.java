package com.xyz.carrental.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyz.carrental.booking.dto.BookingRequest;
import com.xyz.carrental.booking.entity.CarSegment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private com.xyz.carrental.booking.service.BookingService service;

    @BeforeEach
    void setup() {
    }

    @Test
    void confirmEndpoint_returnsOk() throws Exception {
        BookingRequest req = new BookingRequest("DL123", 30, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), CarSegment.SMALL);
        when(service.confirmBooking(any())).thenReturn("abc-123");

        mockMvc.perform(post("/api/v1/bookings/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(service, times(1)).confirmBooking(any());
    }
}
