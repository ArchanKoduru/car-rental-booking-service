package com.xyz.carrental.carpricing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RateResponse {
    private String category;
    private BigDecimal ratePerDay;
}


