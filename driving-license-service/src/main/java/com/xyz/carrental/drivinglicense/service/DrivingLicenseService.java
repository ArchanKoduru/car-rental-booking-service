package com.xyz.carrental.drivinglicense.service;



import com.xyz.carrental.drivinglicense.entity.DrivingLicense;
import com.xyz.carrental.drivinglicense.exception.LicenseNotFoundException;
import com.xyz.carrental.drivinglicense.repository.DrivingLicenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrivingLicenseService {

    private final DrivingLicenseRepository repository;

    public DrivingLicense getLicense(String licenseNumber) {
        return repository.findById(licenseNumber)
                .orElseThrow(() -> new LicenseNotFoundException("Driving license not found"));
    }
}
