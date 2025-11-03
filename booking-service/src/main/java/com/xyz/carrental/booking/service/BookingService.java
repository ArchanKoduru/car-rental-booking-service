package com.xyz.carrental.booking.service;

import com.xyz.carrental.booking.client.CarPricingClient;
import com.xyz.carrental.booking.client.DrivingLicenseClient;
import com.xyz.carrental.booking.dto.BookingRequest;
import com.xyz.carrental.booking.dto.BookingResponse;
import com.xyz.carrental.booking.entity.Booking;
import com.xyz.carrental.booking.exception.CarCategoryNotFoundException;
import com.xyz.carrental.booking.exception.DrivingLicenseNotFoundException;
import com.xyz.carrental.booking.exception.InvalidBookingException;
import com.xyz.carrental.booking.external.LicenseResponse;
import com.xyz.carrental.booking.external.RateResponse;
import com.xyz.carrental.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepository repository;
    private final DrivingLicenseClient licenseClient;
    private final CarPricingClient pricingClient;

    public String confirmBooking(BookingRequest request) {
        validateRequestDates(request);

        LicenseResponse license = licenseClient.validateLicense(request.getLicenseNumber());

        if (license == null) {
            throw new DrivingLicenseNotFoundException("Driving license not found: " + request.getLicenseNumber());
        }

        log.info("[confirmBooking] license response: ", license);

        LocalDate assumedIssueDate = license.getExpiryDate().minusYears(10);
        System.out.println("License expiry date: " + license.getExpiryDate());
        if (assumedIssueDate.isAfter(LocalDate.now().minusYears(1))) {
            throw new InvalidBookingException("Driving license must be at least 1 year old");
        }

        // Ensure the license has not expired (expiry date must be today or in the future)
        if (license.getExpiryDate().isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Driving license has already expired");
        }

        // Ensure the license does not expire within the next 30 days
        if (license.getExpiryDate().isBefore(LocalDate.now().plusDays(30))) {
            throw new InvalidBookingException("Driving license is expiring within the next 30 days");
        }

        System.out.println("License expiry date: " + license.getExpiryDate());
        // License must be valid for at least 1 more year

        RateResponse rateResp = pricingClient.getCarPricing(request.getCarSegment().name());
        if (rateResp == null || rateResp.getRatePerDay() == null) {
            throw new CarCategoryNotFoundException("Invalid car category: " + request.getCarSegment());
        }

        BigDecimal rate = BigDecimal.valueOf(rateResp.getRatePerDay());

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        BigDecimal total = rate.multiply(BigDecimal.valueOf(days));

        Booking booking = Booking.builder()
                .licenseNumber(request.getLicenseNumber())
                .customerName(license.getOwnerName())
                .age(request.getAge())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .carSegment(request.getCarSegment())
                .rentalPrice(total)
                .build();

        return repository.save(booking).getId();
    }

    public BookingResponse getBooking(String id) {
        Booking b = repository.findById(id)
                .orElseThrow(() -> new InvalidBookingException("Booking not found: " + id));

        return BookingResponse.builder()
                .bookingId(b.getId())
                .licenseNumber(b.getLicenseNumber())
                .customerName(b.getCustomerName())
                .age(b.getAge())
                .startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .carSegment(b.getCarSegment())
                .rentalPrice(b.getRentalPrice())
                .build();
    }

    private void validateRequestDates(BookingRequest req) {
        long days = ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1;
        if (days <= 0) throw new InvalidBookingException("End date must be after or equal to start date");
        if (days > 30) throw new InvalidBookingException("Reservation cannot exceed 30 days");
    }
}

