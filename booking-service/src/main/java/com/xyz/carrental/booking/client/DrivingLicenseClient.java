package com.xyz.carrental.booking.client;
import com.xyz.carrental.booking.external.LicenseResponse;
import com.xyz.carrental.booking.exception.DrivingLicenseNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class DrivingLicenseClient {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public DrivingLicenseClient(RestTemplate restTemplate,
                                @Value("${external.driving-license-api}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public LicenseResponse validateLicense(String licenseNumber) {
        String url = apiUrl + "/" + licenseNumber;
        try {
            return restTemplate.getForObject(url, LicenseResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new DrivingLicenseNotFoundException("Driving license not found: " + licenseNumber);
            }
            throw e;
        }
    }
}

