package com.xyz.carrental.carpricing.repository;


import com.xyz.carrental.carpricing.entity.CarPricing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarPricingRepository extends JpaRepository<CarPricing, String> {
}
