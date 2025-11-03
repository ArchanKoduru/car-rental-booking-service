package com.xyz.carrental.carpricing.service;

import com.xyz.carrental.carpricing.entity.CarPricing;
import com.xyz.carrental.carpricing.exception.CarCategoryNotFoundException;
import com.xyz.carrental.carpricing.repository.CarPricingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarPricingService {

    private final CarPricingRepository repository;

    public CarPricing getRate(String category) {
        return repository.findById(category.toUpperCase())
                .orElseThrow(() -> new CarCategoryNotFoundException("Invalid car category"));
    }
}

