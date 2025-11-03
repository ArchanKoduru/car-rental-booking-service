package com.xyz.carrental.booking.dto;

import com.xyz.carrental.booking.entity.CarSegment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
    @NotBlank(message = "License number must not be empty")
    private String licenseNumber;
    @Min(18)
    private int age;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private CarSegment carSegment;
}

