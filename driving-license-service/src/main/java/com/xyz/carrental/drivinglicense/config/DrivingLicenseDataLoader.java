package com.xyz.carrental.drivinglicense.config;

import com.xyz.carrental.drivinglicense.entity.DrivingLicense;
import com.xyz.carrental.drivinglicense.repository.DrivingLicenseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DrivingLicenseDataLoader implements CommandLineRunner {

    private final DrivingLicenseRepository repository;

    public DrivingLicenseDataLoader(DrivingLicenseRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<DrivingLicense> initialLicenses = List.of(
                new DrivingLicense("DL123", "Holder1", LocalDate.of(2026, 11, 3)),
                new DrivingLicense("DL456", "Holder2", LocalDate.of(2027, 1, 15)),
                new DrivingLicense("DL1234", "Expired DL", LocalDate.of(2014, 11, 3)),
                new DrivingLicense("DL1235", "Invalid DL", LocalDate.of(2035, 10, 3)),
                new DrivingLicense("DL1236", "Invalid DL", LocalDate.of(2025, 11, 27)),
                new DrivingLicense("DL789", "Holder3",  LocalDate.of(2028, 5, 22))
        );

        for (DrivingLicense license : initialLicenses) {
            repository.findById(license.getLicenseNumber())
                    .orElseGet(() -> repository.save(license));
        }

        System.out.println(" Driving license data initialized");
    }
}
