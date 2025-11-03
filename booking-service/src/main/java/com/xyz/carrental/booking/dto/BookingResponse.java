package com.xyz.carrental.booking.dto;

import com.xyz.carrental.booking.entity.CarSegment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponse {
    private String bookingId;
    private String licenseNumber;
    private String customerName;
    private int age;
    private LocalDate startDate;
    private LocalDate endDate;
    private CarSegment carSegment;
    private BigDecimal rentalPrice;
}

