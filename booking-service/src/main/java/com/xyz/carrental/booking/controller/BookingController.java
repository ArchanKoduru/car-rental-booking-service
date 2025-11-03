package com.xyz.carrental.booking.controller;

import com.xyz.carrental.booking.dto.BookingRequest;
import com.xyz.carrental.booking.dto.BookingResponse;
import com.xyz.carrental.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping("/confirm")
    public ResponseEntity<Map<String, String>> confirm(@Valid @RequestBody BookingRequest request) {
        String bookingId = service.confirmBooking(request);
        return ResponseEntity.ok(Map.of("bookingId", bookingId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(service.getBooking(id));
    }
}

