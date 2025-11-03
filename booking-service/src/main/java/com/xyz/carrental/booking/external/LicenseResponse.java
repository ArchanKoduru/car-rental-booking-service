package com.xyz.carrental.booking.external;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseResponse {
    private String ownerName;
    private LocalDate expiryDate;
}
