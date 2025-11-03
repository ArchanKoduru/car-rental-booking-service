package com.xyz.carrental.drivinglicense.repository;


import com.xyz.carrental.drivinglicense.entity.DrivingLicense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivingLicenseRepository extends JpaRepository<DrivingLicense, String> {
}
