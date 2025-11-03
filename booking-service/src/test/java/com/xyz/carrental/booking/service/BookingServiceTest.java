package com.xyz.carrental.booking.service;

import com.xyz.carrental.booking.client.CarPricingClient;
import com.xyz.carrental.booking.client.DrivingLicenseClient;
import com.xyz.carrental.booking.dto.BookingRequest;
import com.xyz.carrental.booking.entity.Booking;
import com.xyz.carrental.booking.entity.CarSegment;
import com.xyz.carrental.booking.exception.*;
import com.xyz.carrental.booking.external.LicenseResponse;
import com.xyz.carrental.booking.external.RateResponse;
import com.xyz.carrental.booking.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository repository;

    @Mock
    private DrivingLicenseClient licenseClient;

    @Mock
    private CarPricingClient pricingClient;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // -----------------------
    // License Not Found
    // -----------------------
    @Test
    void testLicenseNotFound() {
        BookingRequest request = new BookingRequest("DL9999", 25,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                CarSegment.MEDIUM);

        when(licenseClient.validateLicense("DL9999")).thenReturn(null);

        DrivingLicenseNotFoundException ex = assertThrows(
                DrivingLicenseNotFoundException.class,
                () -> bookingService.confirmBooking(request)
        );

        assertEquals("Driving license not found: DL9999", ex.getMessage());
    }

    // -----------------------
    // License Too New (<1 year old)
    // -----------------------
    @Test
    void testLicenseTooNew() {
        BookingRequest request = new BookingRequest("DL1234", 25,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                CarSegment.MEDIUM);

        // License issued less than 1 year ago
        LicenseResponse licenseResponse = new LicenseResponse(
                "John Doe",
                LocalDate.now().plusYears(10).minusMonths(6) // issued 6 months ago
        );
        when(licenseClient.validateLicense("DL1234")).thenReturn(licenseResponse);

        // Mock car pricing to prevent unrelated exception
        when(pricingClient.getCarPricing(anyString()))
                .thenReturn(new RateResponse("MEDIUM", 50.0));

        InvalidBookingException ex = assertThrows(
                InvalidBookingException.class,
                () -> bookingService.confirmBooking(request)
        );

        assertEquals("Driving license must be at least 1 year old", ex.getMessage());
    }

    // -----------------------
    // License Expired
    // -----------------------
    @Test
    void testLicenseExpired() {
        BookingRequest request = new BookingRequest("DL5678", 30,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                CarSegment.MEDIUM);

        // License already expired
        LicenseResponse licenseResponse = new LicenseResponse(
                "Alice Smith",
                LocalDate.now().minusDays(1)
        );
        when(licenseClient.validateLicense("DL5678")).thenReturn(licenseResponse);

        // Mock car pricing
        when(pricingClient.getCarPricing(anyString()))
                .thenReturn(new RateResponse("MEDIUM", 50.0));

        InvalidBookingException ex = assertThrows(
                InvalidBookingException.class,
                () -> bookingService.confirmBooking(request)
        );

        assertEquals("Driving license has already expired", ex.getMessage());
    }

    // -----------------------
    // License Expiring Within 30 Days
    // -----------------------
    @Test
    void testLicenseExpiringSoon() {
        BookingRequest request = new BookingRequest("DL9012", 28,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                CarSegment.MEDIUM);

        LicenseResponse licenseResponse = new LicenseResponse(
                "Bob Johnson",
                LocalDate.now().plusDays(20) // expires in 20 days
        );
        when(licenseClient.validateLicense("DL9012")).thenReturn(licenseResponse);

        // Mock car pricing
        when(pricingClient.getCarPricing(anyString()))
                .thenReturn(new RateResponse("MEDIUM", 50.0));

        InvalidBookingException ex = assertThrows(
                InvalidBookingException.class,
                () -> bookingService.confirmBooking(request)
        );

        assertEquals("Driving license is expiring within the next 30 days", ex.getMessage());
    }

    // -----------------------
    // Invalid Car Category
    // -----------------------
    @Test
    void testInvalidCarCategory() {
        BookingRequest request = new BookingRequest("DL3333", 35,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                CarSegment.MEDIUM);

        LicenseResponse licenseResponse = new LicenseResponse(
                "Charlie Brown",
                LocalDate.now().plusYears(5)
        );
        when(licenseClient.validateLicense("DL3333")).thenReturn(licenseResponse);

        // Car pricing returns null
        when(pricingClient.getCarPricing(anyString())).thenReturn(null);

        CarCategoryNotFoundException ex = assertThrows(
                CarCategoryNotFoundException.class,
                () -> bookingService.confirmBooking(request)
        );

        assertEquals("Invalid car category: MEDIUM", ex.getMessage());
    }

    // -----------------------
    // Invalid Booking Dates
    // -----------------------
    @Test
    void testInvalidBookingDates() {
        BookingRequest request = new BookingRequest("DL4444", 40,
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1), // end date before start date
                CarSegment.MEDIUM);

        InvalidBookingException ex = assertThrows(
                InvalidBookingException.class,
                () -> bookingService.confirmBooking(request)
        );

        assertEquals("End date must be after or equal to start date", ex.getMessage());
    }

    @Test
    void testBookingTooLong() {
        BookingRequest request = new BookingRequest("DL5555", 40,
                LocalDate.now(),
                LocalDate.now().plusDays(31), // more than 30 days
                CarSegment.MEDIUM);

        InvalidBookingException ex = assertThrows(
                InvalidBookingException.class,
                () -> bookingService.confirmBooking(request)
        );

        assertEquals("Reservation cannot exceed 30 days", ex.getMessage());
    }

    // -----------------------
    // Successful Booking
    // -----------------------
    @Test
    void testSuccessfulBooking() {
        BookingRequest request = new BookingRequest("DL6666", 30,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                CarSegment.MEDIUM);

        LicenseResponse licenseResponse = new LicenseResponse(
                "David Green",
                LocalDate.now().plusYears(5)
        );
        when(licenseClient.validateLicense("DL6666")).thenReturn(licenseResponse);

        RateResponse rateResponse = new RateResponse("MEDIUM", 50.0);
        when(pricingClient.getCarPricing(anyString())).thenReturn(rateResponse);

        Booking savedBooking = Booking.builder()
                .id(UUID.randomUUID().toString())
                .licenseNumber("DL6666")
                .customerName("David Green")
                .age(30)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .carSegment(request.getCarSegment())
                .rentalPrice(BigDecimal.valueOf(250.0)) // 5 days * 50
                .build();

        when(repository.save(any())).thenReturn(savedBooking);

        String bookingId = bookingService.confirmBooking(request);
        assertNotNull(bookingId);
        assertEquals(savedBooking.getId(), bookingId);
    }
}
