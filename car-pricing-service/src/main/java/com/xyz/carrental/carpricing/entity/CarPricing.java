package com.xyz.carrental.carpricing.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarPricing {
    @Id
    private String category; // SMALL, MEDIUM, LARGE, EXTRA_LARGE

    @Column(precision = 10, scale = 2)
    private BigDecimal ratePerDay;
}
