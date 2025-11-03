package com.xyz.carrental.booking.client;


import com.xyz.carrental.booking.external.RateResponse;
import com.xyz.carrental.booking.exception.CarCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;


@Service
public class CarPricingClient {
    private final RestTemplate restTemplate;
    private final String apiUrl;

    public CarPricingClient(RestTemplate restTemplate,
                            @Value("${external.car-pricing-api}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public RateResponse getCarPricing(String carCategory) {
        String url = apiUrl + "/" + carCategory;
        try {
            return restTemplate.getForObject(url, RateResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new CarCategoryNotFoundException("Invalid car category: " + carCategory);
            }
            throw e;
        }
    }
}

