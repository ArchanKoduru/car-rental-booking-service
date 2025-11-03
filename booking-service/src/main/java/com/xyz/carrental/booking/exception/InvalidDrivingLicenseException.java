package com.xyz.carrental.booking.exception;

public class InvalidDrivingLicenseException extends RuntimeException {
    public InvalidDrivingLicenseException(String message) {
        super(message);
    }
}