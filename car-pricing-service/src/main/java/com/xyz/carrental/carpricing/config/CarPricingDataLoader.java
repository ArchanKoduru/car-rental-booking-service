package com.xyz.carrental.carpricing.config;

import com.xyz.carrental.carpricing.entity.CarPricing;
import com.xyz.carrental.carpricing.repository.CarPricingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CarPricingDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CarPricingDataLoader.class);
    private final CarPricingRepository repository;

    public CarPricingDataLoader(CarPricingRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<CarPricing> initialPricing = List.of(
                new CarPricing("SMALL", BigDecimal.valueOf(30)),
                new CarPricing("MEDIUM", BigDecimal.valueOf(50)),
                new CarPricing("LARGE", BigDecimal.valueOf(70)),
                new CarPricing("EXTRA_LARGE", BigDecimal.valueOf(100))
        );

        for (CarPricing pricing : initialPricing) {
            repository.findById(pricing.getCategory())
                    .orElseGet(() -> repository.save(pricing));
        }

        log.info("Car pricing data initialized");
    }
}

