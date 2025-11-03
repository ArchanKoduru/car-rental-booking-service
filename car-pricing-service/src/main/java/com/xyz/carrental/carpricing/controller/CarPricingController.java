package com.xyz.carrental.carpricing.controller;


import com.xyz.carrental.carpricing.config.CarPricingDataLoader;
import com.xyz.carrental.carpricing.dto.RateResponse;
import com.xyz.carrental.carpricing.service.CarPricingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rental/rate")
@RequiredArgsConstructor
public class CarPricingController {

    private static final Logger log = LoggerFactory.getLogger(CarPricingDataLoader.class);
    private final CarPricingService service;

    @GetMapping("/{category}")
    public RateResponse getPricing(@PathVariable String category) {
        log.info("[getPricing] category: " + category);
        var rate = service.getRate(category);
        return new RateResponse(rate.getCategory(), rate.getRatePerDay());
    }
}

