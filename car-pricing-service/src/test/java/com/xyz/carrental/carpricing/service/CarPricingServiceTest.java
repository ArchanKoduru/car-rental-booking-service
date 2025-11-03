package com.xyz.carrental.carpricing.service;

import com.xyz.carrental.carpricing.entity.CarPricing;
import com.xyz.carrental.carpricing.exception.CarCategoryNotFoundException;
import com.xyz.carrental.carpricing.repository.CarPricingRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarPricingServiceTest {

    private final CarPricingRepository repository = mock(CarPricingRepository.class);
    private final CarPricingService service = new CarPricingService(repository);

    @Test
    void shouldReturnRateWhenCategoryExists() {
        CarPricing carPricing = new CarPricing("SMALL", BigDecimal.valueOf(30.00));
        when(repository.findById("SMALL")).thenReturn(Optional.of(carPricing));

        CarPricing result = service.getRate("small");

        assertEquals(BigDecimal.valueOf(30.0), result.getRatePerDay());
        verify(repository, times(1)).findById("SMALL");
    }

    @Test
    void shouldThrowWhenCategoryNotFound() {
        when(repository.findById("MEDIUM")).thenReturn(Optional.empty());
        assertThrows(CarCategoryNotFoundException.class, () -> service.getRate("MEDIUM"));
    }
}
