package com.xyz.carrental.carpricing.exception;


public class CarCategoryNotFoundException extends RuntimeException {
    public CarCategoryNotFoundException(String message) {
        super(message);
    }
}
