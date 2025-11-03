package com.xyz.carrental.booking.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", message);
        return new ResponseEntity<>(body, status);
    }

    // Handles invalid JSON or missing fields
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) ex.getCause();
            String fieldName = ife.getPath().get(0).getFieldName();
            String targetType = ife.getTargetType().getSimpleName();
            String invalidValue = ife.getValue().toString();
            String message = String.format("Invalid value '%s' for field '%s'. Expected type: %s",
                    invalidValue, fieldName, targetType);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", message));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid input"));
    }

    // Handles @Valid validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", errorMessage));
    }

    // Driving license not found
    @ExceptionHandler(DrivingLicenseNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLicenseNotFound(DrivingLicenseNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    // Invalid license (e.g., expired, too new)
    @ExceptionHandler(InvalidDrivingLicenseException.class)
    public ResponseEntity<Map<String, String>> handleInvalidLicense(InvalidDrivingLicenseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    // Invalid car category
    @ExceptionHandler(CarCategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCarCategoryNotFound(CarCategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidBookingException.class)
    public ResponseEntity<Map<String, String>> handleInvalidBooking(InvalidBookingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    // Catch-all for unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtherExceptions(Exception ex) {
        // log ex if needed
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error"));
    }
}
