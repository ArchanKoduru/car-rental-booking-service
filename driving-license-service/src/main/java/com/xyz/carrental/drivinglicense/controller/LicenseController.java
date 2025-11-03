package com.xyz.carrental.drivinglicense.controller;

import com.xyz.carrental.drivinglicense.dto.LicenseResponse;
import com.xyz.carrental.drivinglicense.service.DrivingLicenseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/license/details")
@RequiredArgsConstructor
public class LicenseController {

    private static final Logger log = LoggerFactory.getLogger(LicenseController.class);
    private final DrivingLicenseService service;

    @GetMapping("/{licenseNumber}")
    public LicenseResponse getLicense(@PathVariable String licenseNumber) {
        log.info("[getLicense] licenseNumber: "+ licenseNumber);
        var license = service.getLicense(licenseNumber);
        return new LicenseResponse(license.getOwnerName(), license.getExpiryDate());
    }
}


