package com.xyz.carrental.drivinglicense.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrivingLicense {
    @Id
    private String licenseNumber;
    private String ownerName;
    private LocalDate expiryDate;
}