package com.xyz.carrental.booking.exception;

public class CarCategoryNotFoundException extends RuntimeException {
    public CarCategoryNotFoundException(String message) {
        super(message);
    }
}
