package com.xyz.carrental.drivinglicense.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LicenseResponse {
    private String ownerName;
    private LocalDate expiryDate;
}
